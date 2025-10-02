package crying.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.sound.SoundCategory;

import org.jetbrains.annotations.Nullable;

import crying.Crying;

public class GrapplingHookEntity extends PersistentProjectileEntity implements Leashable {
    private static final int MAX_PULL_TICKS = 80;
    private static final int MAX_AGE = 100;
    private boolean attached = false;
    private Vec3d anchorPos = null;
    private int pullTicks = 0;
    private LeashData leashData;

    public GrapplingHookEntity(EntityType<? extends GrapplingHookEntity> type, World world) {
        super(type, world);
        this.setInvulnerable(true);
    }

    public static GrapplingHookEntity createWithOwner(EntityType<? extends GrapplingHookEntity> type, LivingEntity owner, World world) {
        GrapplingHookEntity entity = new GrapplingHookEntity(type, world);
        entity.setOwner(owner);
        entity.attachLeash(owner, true);
        return entity;
    }

    private void attach(Vec3d pos) {
        this.anchorPos = pos;
        this.attached = true;
        this.setVelocity(0, 0, 0);
        this.setNoGravity(true);
        this.getEntityWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ARROW_HIT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        this.age = 0;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getEntityWorld().isClient()) 
            return;

        if (attached) {            
            pullTicks++;
            if (pullTicks > MAX_PULL_TICKS) {
                this.discard();
                return;
            }

            Entity entity = this.getOwner();
            if (entity == null || !(entity instanceof PlayerEntity)) {
                this.discard();
                return;
            }
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isDead()) {
                this.discard();
                return;
            }
            if (anchorPos == null) {
                return;
            }
            double distSq = player.squaredDistanceTo(anchorPos.x, anchorPos.y, anchorPos.z);
            if (distSq < 1.0) {
                player.setVelocity(0, player.getVelocity().y, 0);
                this.discard();
                return;
            }

            Vec3d dir = anchorPos.subtract(player.getEntityPos());
            double dist = Math.sqrt(distSq);
            Vec3d pullVec = dir.normalize().multiply(Math.min(1.2, 0.9 + dist / 10.0));
            Vec3d blended = player.getVelocity().multiply(0.3).add(pullVec.multiply(0.7));

            player.setVelocity(blended);
            player.fallDistance = 0;
            player.velocityModified = true;

            if (this.age % 10 == 0) {
                this.getEntityWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_CHAIN_PLACE, SoundCategory.PLAYERS, 0.2f, 1.0f);
            }

            return;
        }

        if (this.age > MAX_AGE) {
            this.discard();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (this.getEntityWorld().isClient()) 
            return;

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) hitResult;
            BlockPos hitPos = bhr.getBlockPos();
            BlockState state = this.getEntityWorld().getBlockState(hitPos);

            Vec3d pos = bhr.getPos(); 
            if (state.isIn(BlockTags.CLIMBABLE) || state.isIn(BlockTags.LEAVES) || state.isSolid()) {
                attach(pos);
            }
        } 
        else if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            if (this.isOwner(entityHitResult.getEntity()))
                this.attach(entityHitResult.getEntity().getEntityPos());
            else
                this.discard();
        }
    }

    @Override
    public boolean applyElasticity(Entity leashHolder, LeashData leashData) {
        return false;
    }

    @Override
    public void writeCustomData(WriteView nbt) {
        super.writeCustomData(nbt);
        if (anchorPos != null)
            nbt.put("anchorPos", Vec3d.CODEC, anchorPos);
        nbt.putInt("pullTicks", pullTicks);
        nbt.putBoolean("attached", attached);
    }

    @Override
    public void readCustomData(ReadView nbt) {
        super.readCustomData(nbt);
        anchorPos = nbt.read("anchorPos", Vec3d.CODEC).orElse(new Vec3d(0d, 0d, 0d));
        pullTicks = nbt.getInt("pullTicks", 0);
        attached = nbt.getBoolean("attached", false);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.getOwner() != null && this.getOwner() instanceof PlayerEntity player) {
            if (this.isOwner(player)) {
                Crying.getHook(player).setHook(null);
                player.getInventory().forEach((stack) -> {
                    if (stack.getOrDefault(Crying.HOOK_UUID, "").equals(this.getUuidAsString())) {
                        stack.set(Crying.THROWN, false);
                    }
                });
            }
        }
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        Entity entity = this.getOwner();
        return new EntitySpawnS2CPacket(this, entityTrackerEntry, entity == null ? 0 : entity.getId());
    }

    @Override
    protected void onEntityHit(EntityHitResult result) {
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Crying.CRYING_GRAPPLING_HOOK);
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);
        if (entity != null && entity instanceof PlayerEntity player) {
            Crying.getHook(player).setHook(this);
        }
    }

    @Override
    public void detachLeash() {
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
    protected double getGravity() {
        return 0.075d;
    }

    @Override
    public double getLeashSnappingDistance() {
        return (double) Integer.MAX_VALUE;
    }

    public boolean isAttached() {
        return this.attached;
    }
}