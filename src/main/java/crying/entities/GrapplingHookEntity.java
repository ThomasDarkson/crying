package crying.entities;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GrapplingHookEntity extends AbstractArrow implements Leashable {
    private static final int MAX_PULL_TICKS = 80;
    private static final int MAX_AGE = 100;
    private boolean attached = false;
    private Vec3 anchorPos = null;
    private int pullTicks = 0;
    private LeashData leashData;

    public GrapplingHookEntity(EntityType<? extends GrapplingHookEntity> type, Level world) {
        super(type, world);
        this.setInvulnerable(true);
    }

    public static GrapplingHookEntity createWithOwner(EntityType<? extends GrapplingHookEntity> type, LivingEntity owner, Level world) {
        GrapplingHookEntity entity = new GrapplingHookEntity(type, world);
        entity.setOwner(owner);
        entity.setLeashedTo(owner, true);
        return entity;
    }

    private void attach(Vec3 pos) {
        this.anchorPos = pos;
        this.attached = true;
        this.setDeltaMovement(0, 0, 0);
        this.setNoGravity(true);
        this.level().playSound(null, this.blockPosition(), SoundEvents.ARROW_HIT, SoundSource.PLAYERS, 1.0f, 1.0f);
        this.tickCount = 0;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) 
            return;

        if (attached) {            
            pullTicks++;
            if (pullTicks > MAX_PULL_TICKS) {
                this.discard();
                return;
            }

            Entity entity = this.getOwner();
            if (entity == null || !(entity instanceof Player)) {
                this.discard();
                return;
            }
            Player player = (Player) entity;
            if (player.isDeadOrDying()) {
                this.discard();
                return;
            }
            if (anchorPos == null) {
                return;
            }
            double distSq = player.distanceToSqr(anchorPos.x, anchorPos.y, anchorPos.z);
            if (distSq < 1.0) {
                player.setDeltaMovement(0, player.getDeltaMovement().y, 0);
                this.discard();
                return;
            }

            Vec3 dir = anchorPos.subtract(player.position());
            double dist = Math.sqrt(distSq);
            Vec3 pullVec = dir.normalize().scale(Math.min(1.2, 0.9 + dist / 10.0));
            Vec3 blended = player.getDeltaMovement().scale(0.3).add(pullVec.scale(0.7));

            player.setDeltaMovement(blended);
            player.fallDistance = 0;
            player.hurtMarked = true;

            if (this.tickCount % 10 == 0) {
                this.level().playSound(null, player.blockPosition(), SoundEvents.CHAIN_PLACE, SoundSource.PLAYERS, 0.2f, 1.0f);
            }

            return;
        }

        if (this.tickCount > MAX_AGE) {
            this.discard();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if (this.level().isClientSide()) 
            return;

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) hitResult;
            BlockPos hitPos = bhr.getBlockPos();
            BlockState state = this.level().getBlockState(hitPos);

            Vec3 pos = bhr.getLocation(); 
            if (state.is(BlockTags.CLIMBABLE) || state.is(BlockTags.LEAVES) || state.isSolid()) {
                attach(pos);
            }
        } 
        else if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            if (this.ownedBy(entityHitResult.getEntity()))
                this.attach(entityHitResult.getEntity().position());
            else
                this.discard();
        }
    }

    @Override
    public boolean checkElasticInteractions(Entity leashHolder, LeashData leashData) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        if (anchorPos != null)
            nbt.store("anchorPos", Vec3.CODEC, anchorPos);
        nbt.putInt("pullTicks", pullTicks);
        nbt.putBoolean("attached", attached);
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        anchorPos = nbt.read("anchorPos", Vec3.CODEC).orElse(new Vec3(0d, 0d, 0d));
        pullTicks = nbt.getIntOr("pullTicks", 0);
        attached = nbt.getBooleanOr("attached", false);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.getOwner() != null && this.getOwner() instanceof Player player) {
            if (this.ownedBy(player)) {
                Crying.getHook(player).setHook(null);
                player.getInventory().forEach((stack) -> {
                    if (stack.getOrDefault(Crying.HOOK_UUID, "").equals(this.getStringUUID())) {
                        stack.set(Crying.THROWN, false);
                    }
                });
            }
        }
    }

    @Override
    public void playerTouch(Player player) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entityTrackerEntry) {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket(this, entityTrackerEntry, entity == null ? 0 : entity.getId());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Crying.CRYING_GRAPPLING_HOOK);
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);
        if (entity != null && entity instanceof Player player) {
            Crying.getHook(player).setHook(this);
        }
    }

    @Override
    public void dropLeash() {
    }

    @Override
    public boolean isLeashed() {
        return getLeashHolder() != null;
    }

    @Override
    public Entity getLeashHolder() {
      return this.getOwner();
    }

    @Override
    public LeashData getLeashData() {
        return this.leashData;
    }

    @Override
    public void setLeashData(LeashData data) {
        this.leashData = data;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.075d;
    }

    @Override
    public double leashSnapDistance() {
        return (double) Integer.MAX_VALUE;
    }

    public boolean isAttached() {
        return this.attached;
    }
}