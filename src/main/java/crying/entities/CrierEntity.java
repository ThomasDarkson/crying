package crying.entities;

import java.util.ArrayList;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import crying.goals.FastBreakDoorGoal;
import crying.interfaces.NbtInterface;
import crying.other.CrierExplosionBehavior;
import crying.tools.CryingShieldItem;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.entity.boss.BossBar.Style;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.NbtReadView;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.explosion.Explosion;

public class CrierEntity extends HostileEntity {
    ServerBossBar bossBar = null;
    ServerBossBar shieldBar = null;

    private static final TrackedData<Float> shieldHealth;
    private static final TrackedData<Boolean> secondPhase;
    private boolean initializedExplosion = false;
    private boolean isSmall = false;
    private boolean healing = false;
    private int healingTicks = 0;
    private ArrayList<String> gotAttackedByPlayer = new ArrayList<>();

    static {
        secondPhase = DataTracker.registerData(CrierEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        shieldHealth = DataTracker.registerData(CrierEntity.class, TrackedDataHandlerRegistry.FLOAT);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
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
        this.targetSelector.add(4, new ActiveTargetGoal(this, EndermanEntity.class, false));
        this.targetSelector.add(3, new ActiveTargetGoal(this, CatEntity.class, false));
        this.targetSelector.add(2, new ActiveTargetGoal(this, IronGolemEntity.class, false));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(1, new ActiveTargetGoal(this, WardenEntity.class, false));
        this.goalSelector.add(3, new FlyGoal(this, 1.0));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));

        EntityAttributeInstance instance = this.getAttributeInstance(EntityAttributes.SCALE);
        if (world.getRandom().nextFloat() < 0.005F) {
            instance.setBaseValue(6d);
        }

        double extra = 0d;
        if (world.getPlayers().size() > 1)
            extra = 25d * (world.getPlayers().size() - 1);
        
        double health = 618d + extra;
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(health);
        this.setHealth((float) health);

        this.bossBar = new ServerBossBar(this.getDisplayName(), Color.PURPLE, Style.PROGRESS);
        bossBar.setVisible(true);
        bossBar.setDragonMusic(false);
        bossBar.setPercent(0.0F);

        this.shieldBar = new ServerBossBar(Text.translatable("bar.shield.health"), Color.PURPLE, Style.PROGRESS);
        shieldBar.setVisible(true);
        shieldBar.setDragonMusic(false);
        shieldBar.setPercent(0.0F);

        setCanBreakDoors(true);

        if (this.getEntityWorld().getDimensionEntry().getKey().get() != DimensionTypes.OVERWORLD) {
            this.getEntityWorld().setBlockState(this.getBlockPos(), Blocks.CRYING_OBSIDIAN.getDefaultState());
            this.getEntityWorld().addParticleClient(ParticleTypes.FALLING_OBSIDIAN_TEAR, this.getX(), this.getX(), this.getZ(), 1d, 1d, 1d);
            this.playSound(getDeathSound());
            this.discard();
        }
    }

    public static DefaultAttributeContainer.Builder createCrierAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.FOLLOW_RANGE, 37.0d).add(EntityAttributes.MOVEMENT_SPEED, 0.345d).add(EntityAttributes.FLYING_SPEED, 3.4d).add(EntityAttributes.SPAWN_REINFORCEMENTS, 0d).add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.3d).add(EntityAttributes.ATTACK_DAMAGE, 1d).add(EntityAttributes.WATER_MOVEMENT_EFFICIENCY, 1d);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(secondPhase, false);
        builder.add(shieldHealth, (float) CryingShieldItem.CRYING_SHIELD_HEALTH);
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.equals(StatusEffects.WEAKNESS) && effect.getAmplifier() == 2;
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
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        Random r = world.getRandom();
        this.initEquipment(r, difficulty);
        this.setCanBreakDoors(true);
        return entityData;
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        this.equipStack(EquipmentSlot.HEAD, new ItemStack(Crying.CRYING_HELMET));
    }

    @Override
    protected void mobTick(ServerWorld world) {
        super.mobTick(world);

        if (!this.isOnGround())
            this.setPose(EntityPose.GLIDING);
        else
            this.setPose(EntityPose.STANDING);

        if (this.getTarget() != null && this.getTarget() instanceof PlayerEntity player) {
            isSmall = player.isInSwimmingPose() && !player.isSubmergedInWater();
            changeScale();
        }

        if (!initializedExplosion) {
            world.createExplosion(this, Explosion.createDamageSource(world, this), new CrierExplosionBehavior(false), this.getX(), this.getEyeY(), this.getZ(), 4.5F, false, ExplosionSourceType.MOB);
            initializedExplosion = true;
        }

        float health = this.getHealth() / this.getMaxHealth();
        this.bossBar.setPercent(health);

        this.shieldBar.setPercent(this.getShieldHealth() / (float) CryingShieldItem.CRYING_SHIELD_HEALTH);

        if (health > 0F) {
            if (health < 0.2F && !getSecondPhase()) {
                world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), Crying.CRIER_SCREAM_EVENT, this.getSoundCategory(), 1.0F, 1.0F);
                world.createExplosion(this, Explosion.createDamageSource(world, this), new CrierExplosionBehavior(true), this.getX(), this.getEyeY(), this.getZ(), 2.25F, false, ExplosionSourceType.MOB);
                switchToSecondPhase();

                setSecondPhase(true);
            }
        }

        if (healing) {
            healingTicks++;
            this.setHealth(this.getHealth() + 0.5F);

            if ((healingTicks - 1) % 4 == 0 || healingTicks >= 32)
            {
                getEntityWorld().playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_GENERIC_DRINK, this.getSoundCategory(), 1.0F, 1.0F);          
            }
            if ((healingTicks - 1) % 8 == 0 || healingTicks >= 32)
            {
                this.swingHand(Hand.MAIN_HAND);   
            }

            if (healingTicks >= 32) {
                healing = false;

                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Crying.CRIERS_SWORD));

                EntityAttributeInstance instance = this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
                instance.setBaseValue(0.355d);

                EntityAttributeInstance instance2 = this.getAttributeInstance(EntityAttributes.FLYING_SPEED);
                instance2.setBaseValue(3d);
            }
        }
    }

    void damageShield(float amount) {
        if (amount < 3F) 
            this.setShieldHealth(this.getShieldHealth() - 1F);
        else 
            this.setShieldHealth(this.getShieldHealth() - Math.round(amount));

        this.getEntityWorld().playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_SHIELD_BLOCK, this.getSoundCategory(), 1.0F, 1.0F);
        if (this.getShieldHealth() <= 0)
            breakShield(true);
    }

    void breakShield(boolean playSound) {
        this.shieldBar.setVisible(false);
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Crying.CRIERS_SWORD));
        this.setShieldHealth(0);
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, -1, 2, false, false, false), this);

        if (playSound)
            this.getEntityWorld().playSound(null, getBlockPos(), SoundEvents.ITEM_SHIELD_BREAK.value(), getSoundCategory());
    }

    void changeScale() {
        EntityAttributeInstance scale = this.getAttributeInstance(EntityAttributes.SCALE);
        scale.setBaseValue(isSmall ? 0.495d : 1);

        if (isSmall)
            this.setPose(EntityPose.CROUCHING);
    }

    void switchToSecondPhase() {
        EntityAttributeInstance damage = this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE);
        damage.setBaseValue(4d);

        bossBar.setName(Text.translatable("entity.crying.forlorn.crier").append(this.getDefaultName()));
    }

    void swingBothHands() {
        this.swingHand(Hand.MAIN_HAND);
        this.swingHand(Hand.OFF_HAND);
    }

    public float getShieldHealth() {
        return this.getDataTracker().get(shieldHealth);
    }

    public void setShieldHealth(float health) {
        this.getDataTracker().set(shieldHealth, health);
    }

    public boolean getSecondPhase() {
        return this.getDataTracker().get(secondPhase);
    }

    public void setSecondPhase(boolean phase) {
        this.getDataTracker().set(secondPhase, phase);
    }

    @Override
    protected void dropLoot(ServerWorld world, DamageSource damageSource, boolean causedByPlayer) {
        if (causedByPlayer) {
            ItemStack stack = new ItemStack(Crying.EYE);
            stack.setCount(gotAttackedByPlayer.size());
            this.dropStack(world, stack);

            ItemStack heart = new ItemStack(Crying.CRIERS_HEART);
            heart.setCount(1);
            this.dropStack(world, heart);
        }
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);

        this.bossBar.addPlayer(player);
        this.shieldBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);

        this.bossBar.removePlayer(player);
        this.shieldBar.removePlayer(player);
    }

    @Override
    protected void writeCustomData(WriteView nbt) {
        super.writeCustomData(nbt);

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
    protected void readCustomData(ReadView nbt) {
        super.readCustomData(nbt);

        if (nbt instanceof NbtReadView nbtReadView) {
            ((NbtInterface) nbtReadView).getNbt().forEach((string, element) -> {
                if (string.startsWith("UUID_SET_BY_CRYING_") && element.asBoolean().orElse(false)) {
                    gotAttackedByPlayer.add(string.replace("UUID_SET_BY_CRYING_", ""));
                }
            });
        }

        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }

        initializedExplosion = nbt.getBoolean("initializedExplosion", false);
        healing = nbt.getBoolean("healing", false);

        if (healing)
            heal();

        healingTicks = nbt.getInt("healingTicks", 0);

        setSecondPhase(nbt.getBoolean("secondPhase", false));
        if (getSecondPhase())
            switchToSecondPhase();
        
        isSmall = nbt.getBoolean("isSmall", false);
        changeScale();

        this.setShieldHealth(nbt.getFloat("shieldHealth", (float) CryingShieldItem.CRYING_SHIELD_HEALTH));
        if (this.getShieldHealth() <= 0)
            breakShield(false);
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);

        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    public boolean isCustomNameVisible() {
        return super.isCustomNameVisible() && !getSecondPhase();
    }
    
    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        boolean bl = super.tryAttack(world, target);
        if (bl && target instanceof PlayerEntity) {
            float chance = Math.abs(world.getRandom().nextFloat());
            if (chance < 0.2F) {
                float f = this.getEntityWorld().getLocalDifficulty(this.getBlockPos()).getLocalDifficulty();
                ((PlayerEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100 * (int) f), this);
            }
        }

        return bl;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if ((getHealth() - amount) <= 0 && !(source.getAttacker() instanceof PlayerEntity || source.isOf(DamageTypes.GENERIC))) {
            this.setHealth(0.01F);
            if (!healing)
                heal();
            return false;
        }
        if (isInsideWall()) 
            return false;
        else if (healing)
            return false;
        else if (source.isIn(DamageTypeTags.IS_FIRE))
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
        else if (!source.isIn(DamageTypeTags.BYPASSES_SHIELD) && this.getShieldHealth() > 0) {
            damageShield(amount);
            return false;
        }
        else {
            float f = world.getRandom().nextFloat();
            if (f < (getSecondPhase() ? 0F : 0.045F)) {
                world.createExplosion(this, Explosion.createDamageSource(world, this), new CrierExplosionBehavior(true), this.getX(), this.getEyeY(), this.getZ(), 2F, false, ExplosionSourceType.MOB);
            }
            if (f < (getSecondPhase() ? 0.25F : 0.05F)) {
                heal();
            }

            if (source.getAttacker() instanceof PlayerEntity player) {
                if (!gotAttackedByPlayer.contains(player.getUuidAsString()))
                    gotAttackedByPlayer.add(player.getUuidAsString());
            }
            return super.damage(world, source, amount);
        }
    }

    void heal() {
        if (healing)
            return;
            
        healing = true;
        healingTicks = 0;

        this.equipStack(EquipmentSlot.MAINHAND, PotionContentsComponent.createStack(Items.POTION, Potions.HEALING));

        EntityAttributeInstance instance = this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
        instance.setBaseValue(0.001d);

        EntityAttributeInstance instance2 = this.getAttributeInstance(EntityAttributes.FLYING_SPEED);
        instance2.setBaseValue(0.001d);
    }

    public void setCanBreakDoors(boolean canBreakDoors) {
        if (NavigationConditions.hasMobNavigation(this)) {
            ((MobNavigation)this.getNavigation()).setCanOpenDoors(true);
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
    public boolean canPickupItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canGather(ServerWorld world, ItemStack stack) {
        return false;
    }

    @Override
    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer) {
    }

    public static boolean shouldBeBaby(Random random) {
        return false;
    }
}