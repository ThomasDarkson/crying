package crying.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import crying.Crying;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class GranterEntity extends Entity implements FlyingItemEntity {
    private static final HashMap<String, GranterEntity> map = new HashMap<>();
    private static final int FOLLOW_TICKS = 100;
    private int age = 0;
    private boolean descending = false;

    private String targetUuid;
    private LivingEntity target;

    public GranterEntity(EntityType<? extends GranterEntity> type, World world) {
        super(type, world);
        this.noClip = true;
    }

    private static GranterEntity createGranterEntity(World world, LivingEntity target) {
        GranterEntity granter = new GranterEntity(Crying.GRANTER_ENTITY, world);
        granter.setTarget(target);
        granter.setPosition(target.getX(), target.getY() + 5d, target.getZ()); 

        map.put(target.getUuidAsString(), granter);
        return granter;
    }

    private static EvilGranterEntity createEvilGranterEntity(World world, LivingEntity target) {
        EvilGranterEntity granter = new EvilGranterEntity(Crying.EVIL_GRANTER_ENTITY, world);
        granter.setTarget(target);
        granter.setPosition(target.getX(), target.getY() + 5d, target.getZ()); 

        map.put(target.getUuidAsString(), granter);
        return granter;
    }

    public static GranterEntity summonGranterEntity(World world, LivingEntity target) {
        if (map.containsKey(target.getUuidAsString()) && map.get(target.getUuidAsString()).getEntityWorld() == target.getEntityWorld()) {
            return null;
        }

        GranterEntity entity = createGranterEntity(world, target);
        world.spawnEntity(entity);
        return entity;
    }

    public static EvilGranterEntity summonEvilGranterEntity(World world, LivingEntity target) {
        if (map.containsKey(target.getUuidAsString()) && map.get(target.getUuidAsString()).getEntityWorld() == target.getEntityWorld()) {
            return null;
        }

        EvilGranterEntity entity = createEvilGranterEntity(world, target);
        world.spawnEntity(entity);
        return entity;
    }

    @Override
    public void tick() {
        super.tick();

        World world = this.getEntityWorld();
        if (!world.isClient()) {
            if (target == null && targetUuid != null) {
                try {
                    target = ((LivingEntity) world.getEntity(UUID.fromString(targetUuid)));
                }
                catch(Exception e) {

                }
            }
            if (target == null)
                target = world.getClosestPlayer(this, 64);
            if (target == null || target.isDead()) {
                this.remove(RemovalReason.UNLOADED_WITH_PLAYER);
                return;
            }
            else
                targetUuid = target.getUuidAsString();

            age++;
            if (!descending) {
                if (age < FOLLOW_TICKS) {
                    double bob = Math.sin(this.age * 0.2) * 0.25;
                    Vec3d targetEntityPos = target.getEntityPos().add(0, 3d + bob, 0);

                    double speed = 0.4d;
                    Vec3d newPos = this.getEntityPos().lerp(targetEntityPos, speed);
                    this.setPosition(newPos);
                } 
                else {
                    age = 0;
                    descending = true;
                }
            } 
            else {
                Vec3d currentPos = this.getEntityPos();
                Vec3d targetEntityPos = target.getEntityPos().add(0, 1.0, 0);
                Vec3d direction = targetEntityPos.subtract(currentPos).normalize().multiply(0.2d);

                this.setVelocity(direction);
                this.move(MovementType.SELF, this.getVelocity());

                if (this.getBoundingBox().intersects(target.getBoundingBox())) {
                    this.remove(RemovalReason.KILLED);
                }
                else if (target.isDead()) {
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
            ArrayList<StatusEffect> effects = new ArrayList<>();
            StatusEffect effect = null;
            for (StatusEffect e : Registries.STATUS_EFFECT) {
                if (e.getCategory() == (isEvil() ? StatusEffectCategory.HARMFUL : StatusEffectCategory.BENEFICIAL)) {
                    effects.add(e);
                }
            }
            if (!isEvil()) {
                effects.addAll(Collections.nCopies(3, StatusEffects.REGENERATION.value()));
                effects.addAll(Collections.nCopies(3, StatusEffects.SATURATION.value()));
                effects.addAll(Collections.nCopies(3, StatusEffects.INSTANT_HEALTH.value()));
            }

            Random random = this.getEntityWorld().getRandom();
            while (effect == null) {
                effect = effects.get(random.nextInt(effects.size()));
            }

            this.getEntityWorld().addParticleClient(ParticleTypes.GLOW, target.getX(), target.getY(), target.getZ(), 1d, 1d, 1d);
            this.target.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(effect), random.nextBetween(5, 30) * 20, random.nextBetween(0, 1)));
            if (!isEvil()) {
                this.target.heal(2F);
            }

            this.getEntityWorld().playSound(this, BlockPos.ofFloored(this.getEntityPos().x, this.getEntityPos().y, this.getEntityPos().z), isEvil() ? Crying.EVIL_GRANTER_HEAL_TOUCH : Crying.GRANTER_HEAL_EVENT, SoundCategory.AMBIENT);
        }

        super.remove(reason);
    }

    @Override
    protected void readCustomData(ReadView nbt) {
        age = nbt.getInt("age", 0);
        targetUuid = nbt.getString("targetUuid", null);
        descending = nbt.getBoolean("descending", false);
        if (targetUuid != null)
            map.put(targetUuid, this);
    }

    @Override
    protected void writeCustomData(WriteView nbt) {
        nbt.putInt("age", age);
        nbt.putBoolean("descending", descending);
        if (targetUuid != null)
            nbt.putString("targetUuid", targetUuid);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void initDataTracker(Builder builder) {
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(Crying.GRANTER);
    }

    public String gettargetUuid() {
        return this.targetUuid;
    }

    public void setTarget(LivingEntity entity) {
        if (entity == null)
            return;

        this.target = entity;
        this.targetUuid = entity.getUuidAsString();
    }

    public final boolean isEvil() {
        return this instanceof EvilGranterEntity;
    }
}
