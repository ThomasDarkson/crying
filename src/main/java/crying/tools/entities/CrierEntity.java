package crying.tools.entities;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;

import crying.tools.Crying;
import crying.tools.goals.FastBreakDoorGoal;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.entity.boss.BossBar.Style;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;
import net.minecraft.world.explosion.Explosion;

public class CrierEntity extends ZombieEntity {
    ServerBossBar bossBar = null;

    boolean secondPhase = false;
    boolean initializedExplosion = false;

    boolean summonedHusk = false;
    boolean summonedWitch = false;
    boolean summonedIllusioner = false;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public CrierEntity(EntityType<? extends CrierEntity> entityType, World world) {
        super(entityType, world);

        this.clearGoals((goal) -> {
            return true;
        });

        this.targetSelector.clear((target) -> {
            return true;
        });

        this.moveControl = new FlightMoveControl(this, 1, false);

        this.targetSelector.add(6, new ActiveTargetGoal(this, SnowGolemEntity.class, false));
        this.targetSelector.add(5, new ActiveTargetGoal(this, IronGolemEntity.class, false));
        this.targetSelector.add(4, new ActiveTargetGoal(this, EndermanEntity.class, false));
        this.targetSelector.add(3, new ActiveTargetGoal(this, CatEntity.class, false));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(1, new ActiveTargetGoal(this, WardenEntity.class, false));
        this.goalSelector.add(3, new FlyGoal(this, 1.0));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(2, new ZombieAttackGoal(this, 1.0, false));

        EntityAttributeInstance instance = this.getAttributeInstance(EntityAttributes.SCALE);
        if (world.getRandom().nextFloat() < 0.005F) {
            instance.setBaseValue(6d);
        }

        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), Color.PURPLE, Style.PROGRESS));
        bossBar.setVisible(true);
        bossBar.setDragonMusic(false);
        bossBar.setPercent(0.0F);

        this.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, -1, 1, false, false, false), this);
    }

    public static DefaultAttributeContainer.Builder createCrierAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.FOLLOW_RANGE, 37.0d).add(EntityAttributes.MOVEMENT_SPEED, 0.35500004174432513d).add(EntityAttributes.FLYING_SPEED, 0.67500004174432513d).add(EntityAttributes.MAX_HEALTH, 618d).add(EntityAttributes.SPAWN_REINFORCEMENTS, 0d).add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.3d).add(EntityAttributes.ATTACK_DAMAGE, 1d).add(EntityAttributes.STEP_HEIGHT, 3d).add(EntityAttributes.WATER_MOVEMENT_EFFICIENCY, 1d);
    }

    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.equals(StatusEffects.WEAKNESS) || effect.equals(StatusEffects.INVISIBILITY);
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return Crying.CRIER_IDLE_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return Crying.CRIER_HURT_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return Crying.CRIER_DIES_EVENT;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_WARDEN_STEP;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        Random r = world.getRandom();
        this.initEquipment(r, difficulty);
        this.setCanBreakDoors(true);
        return entityData;
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        this.equipStack(EquipmentSlot.HEAD, new ItemStack(Crying.helmet));
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Crying.sword));
        this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
    }

    @Override
    protected void mobTick(ServerWorld world) {
        super.mobTick(world);

        if (!initializedExplosion) {
            world.createExplosion(this, Explosion.createDamageSource(world, this), new CrierExplosionBehavior(), this.getX(), this.getEyeY(), this.getZ(), 4.5F, false, ExplosionSourceType.MOB, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE);
            initializedExplosion = true;
        }

        float health = this.getHealth() / this.getMaxHealth();

        this.bossBar.setPercent(health);

        if (health > 0F) {
            if (health < 0.75F) {
                if (!summonedHusk) {
                    summonHusks(world, 4);
                    summonedHusk = true;
                }
            }
            if (health < 0.5F) {
                if (!summonedWitch) {
                    summonWitch(world);
                    summonedWitch = true;
                }
            }
            if (health < 0.25F) {
                if (!summonedIllusioner) {
                    summonIllusioner(world);
                    summonedIllusioner = true;
                }
            }

            if (health < 0.1F && !secondPhase) {
                world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), Crying.CRIER_SCREAM_EVENT, this.getSoundCategory(), 1.0F, 1.0F);
                world.createExplosion(this, this.getX(), this.getEyeY(), this.getZ(), 2.5F, true, ExplosionSourceType.MOB);
                switchToSecondPhase();

                secondPhase = true;
            }
        }
    }

    void switchToSecondPhase() {
        EntityAttributeInstance scale = this.getAttributeInstance(EntityAttributes.SCALE);
        scale.setBaseValue(3d);

        EntityAttributeInstance height = this.getAttributeInstance(EntityAttributes.STEP_HEIGHT);
        height.setBaseValue(5d);
        
        EntityAttributeInstance damage = this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE);
        damage.setBaseValue(5d);

        bossBar.setName(Text.translatable("entity.crying.forlorn.crier").append(this.getDefaultName()));
    }

    void swingBothHands() {
        this.swingHand(Hand.MAIN_HAND);
        this.swingHand(Hand.OFF_HAND);
    }

    void summonIllusioner(ServerWorld world) {
        IllusionerEntity illusioner = new IllusionerEntity(EntityType.ILLUSIONER, world);
        illusioner.setPos(getX(), getY(), getZ());
        world.spawnEntity(illusioner);

        swingBothHands();
    }
    
    void summonWitch(ServerWorld world) {
        WitchEntity witch = new WitchEntity(EntityType.WITCH, world);
        witch.setPos(getX(), getY(), getZ());
        world.spawnEntity(witch);

        swingBothHands();
    }

    void summonHusks(ServerWorld world, int count) {
        for (int i = 0; i < count; i++) {
            HuskEntity husk = new HuskEntity(EntityType.HUSK, world);
            husk.setPos(getX(), getY(), getZ());
            world.spawnEntity(husk);
        }

        swingBothHands();
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);

        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);

        this.bossBar.removePlayer(player);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putBoolean("summonedHusk", summonedHusk);
        nbt.putBoolean("summonedWitch", summonedWitch);
        nbt.putBoolean("summonedIllusioner", summonedIllusioner);
        nbt.putBoolean("secondPhase", secondPhase);
        nbt.putBoolean("initializedExplosion", initializedExplosion);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }

        summonedHusk = nbt.getBoolean("summonedHusk");
        summonedWitch = nbt.getBoolean("summonedWitch");
        summonedIllusioner = nbt.getBoolean("summonedIllusioner");
        secondPhase = nbt.getBoolean("secondPhase");
        initializedExplosion = nbt.getBoolean("initializedExplosion");

        if (secondPhase)
            switchToSecondPhase();
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);

        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    public boolean isCustomNameVisible() {
        return super.isCustomNameVisible() && !secondPhase;
    }
    
    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        boolean bl = super.tryAttack(world, target);
        if (bl && target instanceof PlayerEntity) {
            float chance = Math.abs(world.getRandom().nextFloat());
            if (chance < 0.045F) {
                float f = this.getWorld().getLocalDifficulty(this.getBlockPos()).getLocalDifficulty();
                ((PlayerEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 80 * (int)f), this);
            }
        }

        return bl;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (isInsideWall()) 
            return false;
        else if (source.isIn(DamageTypeTags.IS_FIRE) && secondPhase)
            return false;
        else if (source.getWeaponStack() != null && source.getWeaponStack().getItem() == Items.MACE) {
            world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_SHIELD_BLOCK, this.getSoundCategory(), 1.0F, 1.0F);
            swingBothHands();
            return false;
        }
        else if (source.isOf(DamageTypes.DROWN) || source.isIn(DamageTypeTags.IS_FALL))
            return false;
        else if (source.isIn(DamageTypeTags.IS_PROJECTILE)) {
            world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_SHIELD_BLOCK, this.getSoundCategory(), 1.0F, 1.0F);
            return false;
        }
        else if (world.getRandom().nextFloat() < (secondPhase ? 0.6F : 0.2F) && !source.isIn(DamageTypeTags.BYPASSES_SHIELD) && getOffHandStack().getItem() == Items.SHIELD) {
            world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_SHIELD_BLOCK, this.getSoundCategory(), 1.0F, 1.0F);
            this.swingHand(Hand.OFF_HAND);
            return false;
        }
        else {
            float f = world.getRandom().nextFloat();
            if (f < (secondPhase ? 0F : 0.045F)) {
                world.createExplosion(this, this.getX(), this.getEyeY(), this.getZ(), 2F, false, ExplosionSourceType.MOB);
            }
            if (f < (secondPhase ? 0F : 0.05F)) {
                this.swingHand(Hand.MAIN_HAND);
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 100, 0, false, false, false));
            }
            if (f < (secondPhase ? 0.15F : 0.075F)) {
                this.swingHand(Hand.MAIN_HAND);
                this.setHealth(getHealth() + 8F);
            }

            return super.damage(world, source, amount);
        }
    }

    boolean tryAttackT(ServerWorld world, Entity target) {
        float f = (float)this.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
        ItemStack itemStack = this.getWeaponStack();
        if (itemStack == null)
            return false;
        DamageSource damageSource = (DamageSource)Optional.ofNullable(itemStack.getItem().getDamageSource(this)).orElse(this.getDamageSources().mobAttack(this));
        f = EnchantmentHelper.getDamage(world, itemStack, target, damageSource, f);
        f += itemStack.getItem().getBonusAttackDamage(target, f, damageSource);
        boolean bl = target.damage(world, damageSource, f);
        if (bl) {
            float g = this.getKnockbackAgainst(target, damageSource);
            LivingEntity livingEntity;
            if (g > 0.0F && target instanceof LivingEntity) {
                livingEntity = (LivingEntity)target;
                livingEntity.takeKnockback((double)(g * 0.5F), (double)MathHelper.sin(this.getYaw() * 0.017453292F), (double)(-MathHelper.cos(this.getYaw() * 0.017453292F)));
                this.setVelocity(this.getVelocity().multiply(0.6, 1.0, 0.6));
            }

            if (target instanceof LivingEntity) {
                livingEntity = (LivingEntity)target;
                itemStack.postHit(livingEntity, this);
            }

            EnchantmentHelper.onTargetDamaged(world, target, damageSource);
            this.onAttacking(target);
            this.playAttackSound();
        }

        return bl;
    }

    @Override
    protected boolean canConvertInWater() {
        return false;
    }
  
    @Override
    protected void convertInWater() {
    }

    @Override
    public boolean isConvertingInWater() {
        return false;
    }

    @Override
    public boolean canBreakDoors() {
        return true;
    }

    @Override
    public void setCanBreakDoors(boolean canBreakDoors) {
        if (NavigationConditions.hasMobNavigation(this)) {
            ((MobNavigation)this.getNavigation()).setCanPathThroughDoors(true);
            this.goalSelector.add(1, new FastBreakDoorGoal(this, (difficulty) -> {
                return true;
            }));
        }
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    protected int getExperienceToDrop(ServerWorld world) {
        return 0;
    }

    @Override
    public void setBaby(boolean baby) {
    }

    @Override
    protected void convertTo(EntityType<? extends ZombieEntity> entityType) {
    }

    @Override
    public boolean infectVillager(ServerWorld world, VillagerEntity villager) {
        return false;
    }

    @Override
    public boolean canPickupItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canGather(ServerWorld world, ItemStack stack) {
        return false;
    }

    @Override
    protected void applyAttributeModifiers(float chanceMultiplier) {
    }

    @Override
    protected void initAttributes() {
        
    }

    @Override
    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer) {
    }

    public static boolean shouldBeBaby(Random random) {
        return false;
    }
}