package crying.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import crying.Crying;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
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
    private static final int FOLLOW_TICKS = 200;
    private int age = 0;
    private boolean descending = false;

    private String targetPlayerUuid;
    private PlayerEntity targetPlayer;

    public GranterEntity(EntityType<? extends GranterEntity> type, World world) {
        super(type, world);
        this.noClip = true;
    }

    private static GranterEntity createGranterEntity(World world, PlayerEntity target) {
        GranterEntity granter = new GranterEntity(Crying.GRANTER_ENTITY, world);
        granter.setTarget(target);
        granter.setPosition(target.getX(), target.getY() + 5d, target.getZ()); 

        map.put(target.getUuidAsString(), granter);
        return granter;
    }

    public static GranterEntity summonGranterEntity(World world, PlayerEntity target) {
        if (map.containsKey(target.getUuidAsString()) && map.get(target.getUuidAsString()).getWorld() == target.getWorld()) {
            return null;
        }

        GranterEntity entity = createGranterEntity(world, target);
        world.spawnEntity(entity);
        return entity;
    }

    @Override
    public void tick() {
        super.tick();

        World world = this.getWorld();
        if (!world.isClient) {
            if (targetPlayer == null && targetPlayerUuid != null) {
                targetPlayer = world.getPlayerByUuid(UUID.fromString(targetPlayerUuid));
            }
            if (targetPlayer == null)
                targetPlayer = world.getClosestPlayer(this, 64);
            if (targetPlayer == null || targetPlayer.isDead())
                return;
            else
                targetPlayerUuid = targetPlayer.getUuidAsString();

            age++;
            if (!descending) {
                if (age < FOLLOW_TICKS) {
                    double bob = Math.sin(this.age * 0.2) * 0.25;
                    Vec3d targetPos = targetPlayer.getPos().add(0, 3d + bob, 0);

                    world.getClosestPlayer(targetPlayer, bob);
                    double speed = 0.4d;
                    Vec3d newPos = this.getPos().lerp(targetPos, speed);
                    this.setPosition(newPos);
                } 
                else {
                    age = 0;
                    descending = true;
                }
            } 
            else {
                Vec3d currentPos = this.getPos();
                Vec3d targetPos = targetPlayer.getPos().add(0, 1.0, 0);
                Vec3d direction = targetPos.subtract(currentPos).normalize().multiply(0.2d);

                this.setVelocity(direction);
                this.move(MovementType.SELF, this.getVelocity());

                if (this.getBoundingBox().intersects(targetPlayer.getBoundingBox())) {
                    this.remove(RemovalReason.KILLED);
                }
            }
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (map.containsKey(targetPlayerUuid))
            map.remove(targetPlayerUuid);
        
        if (reason == RemovalReason.KILLED) {
            ArrayList<StatusEffect> effects = new ArrayList<>();
            StatusEffect effect = null;
            for (StatusEffect e : Registries.STATUS_EFFECT) {
                if (e.getCategory() == StatusEffectCategory.BENEFICIAL) {
                    effects.add(e);
                }
            }
            effects.addAll(Collections.nCopies(3, StatusEffects.REGENERATION.value()));
            effects.addAll(Collections.nCopies(3, StatusEffects.SATURATION.value()));
            effects.addAll(Collections.nCopies(3, StatusEffects.INSTANT_HEALTH.value()));

            Random random = this.getWorld().getRandom();
            while (effect == null) {
                effect = effects.get(random.nextInt(effects.size()));
            }

            this.getWorld().addParticleClient(ParticleTypes.GLOW, targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(), 1d, 1d, 1d);
            this.targetPlayer.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(effect), random.nextBetween(5, 30) * 20, random.nextBetween(0, 1)));
            this.targetPlayer.heal(2F);
            this.getWorld().playSound(this, BlockPos.ofFloored(this.getPos().x, this.getPos().y, this.getPos().z), Crying.GRANTER_HEAL_EVENT, SoundCategory.PLAYERS);
        }

        super.remove(reason);
    }

    @Override
    protected void readCustomData(ReadView nbt) {
        age = nbt.getInt("age", 0);
        targetPlayerUuid = nbt.getString("targetPlayerUuid", null);
        descending = nbt.getBoolean("descending", false);
        if (targetPlayerUuid != null)
            map.put(targetPlayerUuid, this);
    }

    @Override
    protected void writeCustomData(WriteView nbt) {
        nbt.putInt("age", age);
        nbt.putBoolean("descending", descending);
        if (targetPlayerUuid != null)
            nbt.putString("targetPlayerUuid", targetPlayerUuid);
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

    public String getTargetPlayerUuid() {
        return this.targetPlayerUuid;
    }

    public void setTarget(PlayerEntity entity) {
        if (entity == null)
            return;

        this.targetPlayer = entity;
        this.targetPlayerUuid = entity.getUuidAsString();
    }
}
