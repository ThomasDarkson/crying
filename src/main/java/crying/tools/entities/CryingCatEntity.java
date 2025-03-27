package crying.tools.entities;

import org.jetbrains.annotations.Nullable;

import crying.tools.goals.SleepAndGiveGiftsToPlayerGoal;
import crying.tools.interfaces.PrivateCatFieldsInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.AttackGoal;
import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.GoToBedAndSleepGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.PounceAtTargetGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class CryingCatEntity extends CatEntity {
    private PlayerEntity ogOwner = null;
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public CryingCatEntity(EntityType<? extends CryingCatEntity> entityType, World world) {
        super(entityType, world);

        this.clearGoals((goal) -> {
            return true;
        });

        this.targetSelector.clear((target) -> {
            return true;
        });

        this.goalSelector.add(1, new PounceAtTargetGoal(this, 1F));
        this.goalSelector.add(1, new AttackGoal(this));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.4d, 5F, 2.5F));
        this.goalSelector.add(3, new SleepAndGiveGiftsToPlayerGoal(this));
        this.goalSelector.add(3, new SitGoal(this));
        this.goalSelector.add(4, new SwimGoal(this));
        this.goalSelector.add(5, new GoToBedAndSleepGoal(this, 1.1, 8));
        this.goalSelector.add(6, new CatSitOnBlockGoal(this, 0.8));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 0.7, 0.06F));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));

        this.targetSelector.add(1, new ActiveTargetGoal(this, CreeperEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PhantomEntity.class, true));
    }

    public void setOgOwner(PlayerEntity player) {
        this.ogOwner = player;
    }

    public boolean getHeadDown() {
        return ((PrivateCatFieldsInterface) (Object) this).get_HeadDown();
    }

    public void setHeadDownPublic(boolean down) {
        ((PrivateCatFieldsInterface) (Object) this).set_HeadDown(down);
    }

    public static DefaultAttributeContainer.Builder createCatAttributes() {
        return AnimalEntity.createAnimalAttributes().add(EntityAttributes.MAX_HEALTH, 0.00001d).add(EntityAttributes.MOVEMENT_SPEED, 0.32000001592092857d).add(EntityAttributes.ATTACK_DAMAGE, 10.3d).add(EntityAttributes.WATER_MOVEMENT_EFFICIENCY, 1d);
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.YELLOW;
    }

    @Override
    public void setOwner(@Nullable LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            if (this.ogOwner == player) {
                super.setOwner(player);
            }
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    protected int getExperienceToDrop(ServerWorld world) {
        return 0;
    }

    @Override
    public void setCustomName(@Nullable Text name) {
    }

    @Nullable
    @Override
    public Text getCustomName() {
        return Text.translatable("entity.crying.crying_cat");
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void setCustomNameVisible(boolean visible) {
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public void addDeathParticles() {
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity owner) {
            if (owner == this.getOwner())
                return super.damage(world, source, 1.5f) && source.isDirect();
            else 
                return false;
        }
        else
            return false;
    }

    @Override 
    public void attachLeash(Entity leashHolder, boolean sendPacket) {
        if (leashHolder instanceof PlayerEntity player) {
            if (this.getOwner() == player) {
                super.attachLeash(leashHolder, sendPacket);
            }
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (!this.isRemoved() && !this.dead) {
            this.setInvisible(true);
            Entity entity = damageSource.getAttacker();
            LivingEntity livingEntity = this.getPrimeAdversary();
            if (livingEntity != null) {
                livingEntity.updateKilledAdvancementCriterion(this, damageSource);
            }

            if (this.isSleeping()) {
                this.wakeUp();
            }

            this.dead = true;
            this.getDamageTracker().update();
            World var5 = this.getWorld();
            if (var5 instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld)var5;
                if (entity == null || entity.onKilledOther(serverWorld, this)) {
                    this.emitGameEvent(GameEvent.ENTITY_DIE);
                    this.drop(serverWorld, damageSource);
                    this.onKilledBy(livingEntity);
                }

                this.getWorld().sendEntityStatus(this, (byte)3);
            }

            this.setPose(EntityPose.STANDING);
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Nullable
    @Override
    public CatEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return null;
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return false;
    }
}
