package crying.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import crying.Crying;

public class GranterEntity extends Entity implements ItemSupplier {
    private static final HashMap<String, GranterEntity> map = new HashMap<>();
    private static final int FOLLOW_TICKS = 100;
    private int tickCount = 0;
    private boolean descending = false;

    private String targetUuid;
    private LivingEntity target;

    public GranterEntity(EntityType<? extends GranterEntity> type, Level world) {
        super(type, world);
        this.noPhysics = true;
    }

    private static GranterEntity createGranterEntity(Level world, LivingEntity target) {
        GranterEntity granter = new GranterEntity(Crying.GRANTER_ENTITY, world);
        granter.setTarget(target);
        granter.setPos(target.getX(), target.getY() + 5d, target.getZ()); 

        map.put(target.getStringUUID(), granter);
        return granter;
    }

    private static EvilGranterEntity createEvilGranterEntity(Level world, LivingEntity target) {
        EvilGranterEntity granter = new EvilGranterEntity(Crying.EVIL_GRANTER_ENTITY, world);
        granter.setTarget(target);
        granter.setPos(target.getX(), target.getY() + 5d, target.getZ()); 

        map.put(target.getStringUUID(), granter);
        return granter;
    }

    public static GranterEntity summonGranterEntity(Level world, LivingEntity target) {
        if (map.containsKey(target.getStringUUID()) && map.get(target.getStringUUID()).level() == target.level()) {
            return null;
        }

        GranterEntity entity = createGranterEntity(world, target);
        world.addFreshEntity(entity);
        return entity;
    }

    public static EvilGranterEntity summonEvilGranterEntity(Level world, LivingEntity target) {
        if (map.containsKey(target.getStringUUID()) && map.get(target.getStringUUID()).level() == target.level()) {
            return null;
        }

        EvilGranterEntity entity = createEvilGranterEntity(world, target);
        world.addFreshEntity(entity);
        return entity;
    }

    @Override
    public void tick() {
        super.tick();

        Level world = this.level();
        if (!world.isClientSide) {
            if (target == null && targetUuid != null) {
                try {
                    target = ((LivingEntity) world.getEntity(UUID.fromString(targetUuid)));
                }
                catch(Exception e) {

                }
            }
            if (target == null)
                target = world.getNearestPlayer(this, 64);
            if (target == null || target.isDeadOrDying()) {
                this.remove(RemovalReason.UNLOADED_WITH_PLAYER);
                return;
            }
            else
                targetUuid = target.getStringUUID();

            tickCount++;
            if (!descending) {
                if (tickCount < FOLLOW_TICKS) {
                    double bob = Math.sin(this.tickCount * 0.2) * 0.25;
                    Vec3 targetPos = target.position().add(0, 3d + bob, 0);

                    double speed = 0.4d;
                    Vec3 newPos = this.position().lerp(targetPos, speed);
                    this.setPos(newPos);
                } 
                else {
                    tickCount = 0;
                    descending = true;
                }
            } 
            else {
                Vec3 currentPos = this.position();
                Vec3 targetPos = target.position().add(0, 1.0, 0);
                Vec3 direction = targetPos.subtract(currentPos).normalize().scale(0.2d);

                this.setDeltaMovement(direction);
                this.move(MoverType.SELF, this.getDeltaMovement());

                if (this.getBoundingBox().intersects(target.getBoundingBox())) {
                    this.remove(RemovalReason.KILLED);
                }
                else if (target.isDeadOrDying()) {
                    this.remove(RemovalReason.UNLOADED_WITH_PLAYER);
                }
            }
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (map.containsKey(targetUuid))
            map.remove(targetUuid);
        
        if (reason == RemovalReason.KILLED) {
            ArrayList<MobEffect> effects = new ArrayList<>();
            MobEffect effect = null;
            for (MobEffect e : BuiltInRegistries.MOB_EFFECT) {
                if (e.getCategory() == (isEvil() ? MobEffectCategory.HARMFUL : MobEffectCategory.BENEFICIAL)) {
                    effects.add(e);
                }
            }
            if (!isEvil()) {
                effects.addAll(Collections.nCopies(3, MobEffects.REGENERATION.value()));
                effects.addAll(Collections.nCopies(3, MobEffects.SATURATION.value()));
                effects.addAll(Collections.nCopies(3, MobEffects.INSTANT_HEALTH.value()));
            }

            RandomSource random = this.level().getRandom();
            while (effect == null) {
                effect = effects.get(random.nextInt(effects.size()));
            }

            this.level().addParticle(ParticleTypes.GLOW, target.getX(), target.getY(), target.getZ(), 1d, 1d, 1d);
            this.target.addEffect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect), random.nextIntBetweenInclusive(5, 30) * 20, random.nextIntBetweenInclusive(0, 1)));
            if (!isEvil()) {
                this.target.heal(2F);
            }

            this.level().playSound(this, BlockPos.containing(this.position().x, this.position().y, this.position().z), isEvil() ? Crying.EVIL_GRANTER_HEAL_TOUCH : Crying.GRANTER_HEAL_EVENT, SoundSource.AMBIENT);
        }

        super.remove(reason);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        tickCount = nbt.getIntOr("age", 0);
        targetUuid = nbt.getStringOr("targetUuid", null);
        descending = nbt.getBooleanOr("descending", false);
        if (targetUuid != null)
            map.put(targetUuid, this);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("age", tickCount);
        nbt.putBoolean("descending", descending);
        if (targetUuid != null)
            nbt.putString("targetUuid", targetUuid);
    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void defineSynchedData(Builder builder) {
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Crying.GRANTER);
    }

    public String gettargetUuid() {
        return this.targetUuid;
    }

    public void setTarget(LivingEntity entity) {
        if (entity == null)
            return;

        this.target = entity;
        this.targetUuid = entity.getStringUUID();
    }

    public final boolean isEvil() {
        return this instanceof EvilGranterEntity;
    }
}
