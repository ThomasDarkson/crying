package crying.entities;

import java.util.ArrayList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import crying.Crying;
import crying.goals.FastBreakDoorGoal;
import crying.interfaces.NbtInterface;
import crying.other.CrierExplosionBehavior;
import crying.tools.CryingShieldItem;

public class CrierEntity extends Monster {
    ServerBossEvent bossBar = null;
    ServerBossEvent shieldBar = null;

    private static final EntityDataAccessor<Float> shieldHealth;
    private static final EntityDataAccessor<Boolean> secondPhase;
    private boolean initializedExplosion = false;
    private boolean isSmall = false;
    private boolean healing = false;
    private int healingTicks = 0;
    private ArrayList<String> gotAttackedByPlayer = new ArrayList<>();

    static {
        secondPhase = SynchedEntityData.defineId(CrierEntity.class, EntityDataSerializers.BOOLEAN);
        shieldHealth = SynchedEntityData.defineId(CrierEntity.class, EntityDataSerializers.FLOAT);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public CrierEntity(EntityType<? extends CrierEntity> entityType, Level world) {
        super(entityType, world);

        this.removeAllGoals((goal) -> {
            return true;
        });

        this.targetSelector.removeAllGoals((target) -> {
            return true;
        });

        this.moveControl = new FlyingMoveControl(this, 1, false);

        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, SnowGolem.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, EnderMan.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Cat.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, IronGolem.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Warden.class, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));

        AttributeInstance instance = this.getAttribute(Attributes.SCALE);
        if (world.getRandom().nextFloat() < 0.005F) {
            instance.setBaseValue(6d);
        }

        double extra = 0d;
        if (world.players().size() > 1)
            extra = 25d * (world.players().size() - 1);
        
        double health = 618d + extra;
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
        this.setHealth((float) health);

        this.bossBar = new ServerBossEvent(this.getDisplayName(), BossBarColor.PURPLE, BossBarOverlay.PROGRESS);
        bossBar.setVisible(true);
        bossBar.setPlayBossMusic(false);
        bossBar.setProgress(0.0F);

        this.shieldBar = new ServerBossEvent(Component.translatable("bar.shield.health"), BossBarColor.PURPLE, BossBarOverlay.PROGRESS);
        shieldBar.setVisible(true);
        shieldBar.setPlayBossMusic(false);
        shieldBar.setProgress(0.0F);

        setCanBreakDoors(true);

        if (this.level().dimensionTypeRegistration().unwrapKey().get() != BuiltinDimensionTypes.OVERWORLD) {
            this.level().setBlockAndUpdate(this.blockPosition(), Blocks.CRYING_OBSIDIAN.defaultBlockState());
            this.level().addParticle(ParticleTypes.FALLING_OBSIDIAN_TEAR, this.getX(), this.getX(), this.getZ(), 1d, 1d, 1d);
            this.makeSound(getDeathSound());
            this.discard();
        }
    }

    public static AttributeSupplier.Builder createCrierAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 37.0d).add(Attributes.MOVEMENT_SPEED, 0.345d).add(Attributes.FLYING_SPEED, 3.4d).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0d).add(Attributes.KNOCKBACK_RESISTANCE, 0.3d).add(Attributes.ATTACK_DAMAGE, 1d).add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1d);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(secondPhase, false);
        builder.define(shieldHealth, (float) CryingShieldItem.CRYING_SHIELD_HEALTH);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return effect.is(MobEffects.WEAKNESS) && effect.getAmplifier() == 2;
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        RandomSource r = world.getRandom();
        this.populateDefaultEquipmentSlots(r, difficulty);
        this.setCanBreakDoors(true);
        return entityData;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance localDifficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Crying.CRYING_HELMET));
    }

    @Override
    protected void customServerAiStep(ServerLevel world) {
        super.customServerAiStep(world);

        if (!this.onGround())
            this.setPose(Pose.FALL_FLYING);
        else
            this.setPose(Pose.STANDING);

        if (this.getTarget() != null && this.getTarget() instanceof Player player) {
            isSmall = player.isVisuallySwimming() && !player.isUnderWater();
            changeScale();
        }

        if (!initializedExplosion) {
            world.explode(this, Explosion.getDefaultDamageSource(world, this), new CrierExplosionBehavior(false), this.getX(), this.getEyeY(), this.getZ(), 4.5F, false, ExplosionInteraction.MOB);
            initializedExplosion = true;
        }

        float health = this.getHealth() / this.getMaxHealth();
        this.bossBar.setProgress(health);

        this.shieldBar.setProgress(this.getShieldHealth() / (float) CryingShieldItem.CRYING_SHIELD_HEALTH);

        if (health > 0F) {
            if (health < 0.2F && !getSecondPhase()) {
                world.playSound((Player) null, this.getX(), this.getY(), this.getZ(), Crying.CRIER_SCREAM_EVENT, this.getSoundSource(), 1.0F, 1.0F);
                world.explode(this, Explosion.getDefaultDamageSource(world, this), new CrierExplosionBehavior(true), this.getX(), this.getEyeY(), this.getZ(), 2.25F, false, ExplosionInteraction.MOB);
                switchToSecondPhase();

                setSecondPhase(true);
            }
        }

        if (healing) {
            healingTicks++;
            this.setHealth(this.getHealth() + 0.5F);

            if ((healingTicks - 1) % 4 == 0 || healingTicks >= 32)
            {
                level().playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_DRINK, this.getSoundSource(), 1.0F, 1.0F);          
            }
            if ((healingTicks - 1) % 8 == 0 || healingTicks >= 32)
            {
                this.swing(InteractionHand.MAIN_HAND);   
            }

            if (healingTicks >= 32) {
                healing = false;

                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Crying.CRIERS_SWORD));

                AttributeInstance instance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                instance.setBaseValue(0.355d);

                AttributeInstance instance2 = this.getAttribute(Attributes.FLYING_SPEED);
                instance2.setBaseValue(3d);
            }
        }
    }

    void damageShield(float amount) {
        if (amount < 3F) 
            this.setShieldHealth(this.getShieldHealth() - 1F);
        else 
            this.setShieldHealth(this.getShieldHealth() - Math.round(amount));

        this.level().playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.SHIELD_BLOCK, this.getSoundSource(), 1.0F, 1.0F);
        if (this.getShieldHealth() <= 0)
            breakShield(true);
    }

    void breakShield(boolean playSound) {
        this.shieldBar.setVisible(false);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Crying.CRIERS_SWORD));
        this.setShieldHealth(0);
        this.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, -1, 2, false, false, false), this);

        if (playSound)
            this.level().playSound(null, blockPosition(), SoundEvents.SHIELD_BREAK.value(), getSoundSource());
    }

    void changeScale() {
        AttributeInstance scale = this.getAttribute(Attributes.SCALE);
        scale.setBaseValue(isSmall ? 0.495d : 1);

        if (isSmall)
            this.setPose(Pose.CROUCHING);
    }

    void switchToSecondPhase() {
        AttributeInstance damage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        damage.setBaseValue(4d);

        bossBar.setName(Component.translatable("entity.crying.forlorn.crier").append(this.getTypeName()));
    }

    void swingBothHands() {
        this.swing(InteractionHand.MAIN_HAND);
        this.swing(InteractionHand.OFF_HAND);
    }

    public float getShieldHealth() {
        return this.getEntityData().get(shieldHealth);
    }

    public void setShieldHealth(float health) {
        this.getEntityData().set(shieldHealth, health);
    }

    public boolean getSecondPhase() {
        return this.getEntityData().get(secondPhase);
    }

    public void setSecondPhase(boolean phase) {
        this.getEntityData().set(secondPhase, phase);
    }

    @Override
    protected void dropFromLootTable(ServerLevel world, DamageSource damageSource, boolean causedByPlayer) {
        if (causedByPlayer) {
            ItemStack stack = new ItemStack(Crying.EYE);
            stack.setCount(gotAttackedByPlayer.size());
            this.spawnAtLocation(world, stack);

            ItemStack heart = new ItemStack(Crying.CRIERS_HEART);
            heart.setCount(1);
            this.spawnAtLocation(world, heart);
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);

        this.bossBar.addPlayer(player);
        this.shieldBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);

        this.bossBar.removePlayer(player);
        this.shieldBar.removePlayer(player);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.putBoolean("secondPhase", getSecondPhase());
        nbt.putBoolean("initializedExplosion", initializedExplosion);
        nbt.putBoolean("healing", healing);
        nbt.putBoolean("isSmall", isSmall);
        nbt.putFloat("shieldHealth", this.getShieldHealth());
        nbt.putInt("healingTicks", healingTicks);

        for (String s : gotAttackedByPlayer) {
            nbt.putBoolean("UUID_SET_BY_CRYING_" + s, true);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);

        if (nbt instanceof TagValueInput nbtReadView) {
            ((NbtInterface) nbtReadView).getNbt().forEach((string, element) -> {
                if (string.startsWith("UUID_SET_BY_CRYING_") && element.asBoolean().orElse(false)) {
                    gotAttackedByPlayer.add(string.replace("UUID_SET_BY_CRYING_", ""));
                }
            });
        }

        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }

        initializedExplosion = nbt.getBooleanOr("initializedExplosion", false);
        healing = nbt.getBooleanOr("healing", false);

        if (healing)
            heal();

        healingTicks = nbt.getIntOr("healingTicks", 0);

        setSecondPhase(nbt.getBooleanOr("secondPhase", false));
        if (getSecondPhase())
            switchToSecondPhase();
        
        isSmall = nbt.getBooleanOr("isSmall", false);
        changeScale();

        this.setShieldHealth(nbt.getFloatOr("shieldHealth", (float) CryingShieldItem.CRYING_SHIELD_HEALTH));
        if (this.getShieldHealth() <= 0)
            breakShield(false);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);

        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    public boolean isCustomNameVisible() {
        return super.isCustomNameVisible() && !getSecondPhase();
    }
    
    @Override
    public boolean doHurtTarget(ServerLevel world, Entity target) {
        boolean bl = super.doHurtTarget(world, target);
        if (bl && target instanceof Player) {
            float chance = Math.abs(world.getRandom().nextFloat());
            if (chance < 0.2F) {
                float f = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
                ((Player) target).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100 * (int) f), this);
            }
        }

        return bl;
    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        if ((getHealth() - amount) <= 0 && !(source.getEntity() instanceof Player || source.is(DamageTypes.GENERIC))) {
            this.setHealth(0.01F);
            if (!healing)
                heal();
            return false;
        }
        if (isInWall()) 
            return false;
        else if (healing)
            return false;
        else if (source.is(DamageTypeTags.IS_FIRE))
            return false;
        else if (source.getWeaponItem() != null && source.getWeaponItem().getItem() == Items.MACE) {
            world.playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.SHIELD_BLOCK, this.getSoundSource(), 1.0F, 1.0F);
            swingBothHands();
            return false;
        }
        else if (source.is(DamageTypes.DROWN) || source.is(DamageTypeTags.IS_FALL))
            return false;
        else if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            world.playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.SHIELD_BLOCK, this.getSoundSource(), 1.0F, 1.0F);
            return false;
        }
        else if (!source.is(DamageTypeTags.BYPASSES_SHIELD) && this.getShieldHealth() > 0) {
            damageShield(amount);
            return false;
        }
        else {
            float f = world.getRandom().nextFloat();
            if (f < (getSecondPhase() ? 0F : 0.045F)) {
                world.explode(this, Explosion.getDefaultDamageSource(world, this), new CrierExplosionBehavior(true), this.getX(), this.getEyeY(), this.getZ(), 2F, false, ExplosionInteraction.MOB);
            }
            if (f < (getSecondPhase() ? 0.25F : 0.05F)) {
                heal();
            }

            if (source.getEntity() instanceof Player player) {
                if (!gotAttackedByPlayer.contains(player.getStringUUID()))
                    gotAttackedByPlayer.add(player.getStringUUID());
            }
            return super.hurtServer(world, source, amount);
        }
    }

    void heal() {
        if (healing)
            return;
            
        healing = true;
        healingTicks = 0;

        this.setItemSlot(EquipmentSlot.MAINHAND, PotionContents.createItemStack(Items.POTION, Potions.HEALING));

        AttributeInstance instance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        instance.setBaseValue(0.001d);

        AttributeInstance instance2 = this.getAttribute(Attributes.FLYING_SPEED);
        instance2.setBaseValue(0.001d);
    }

    public void setCanBreakDoors(boolean canBreakDoors) {
        if (GoalUtils.hasGroundPathNavigation(this)) {
            ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
            this.goalSelector.addGoal(1, new FastBreakDoorGoal(this, (difficulty) -> {
                return true;
            }));
        }
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel world) {
        return 0;
    }

    @Override
    public void setBaby(boolean baby) {
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean wantsToPickUp(ServerLevel world, ItemStack stack) {
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel world, DamageSource source, boolean causedByPlayer) {
    }

    public static boolean shouldBeBaby(RandomSource random) {
        return false;
    }
}