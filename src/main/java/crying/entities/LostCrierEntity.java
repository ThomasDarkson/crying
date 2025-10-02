package crying.entities;

import crying.Crying;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

public class LostCrierEntity extends PathAwareEntity implements InventoryOwner {
    SimpleInventory inventory = new SimpleInventory(4);
    
    public LostCrierEntity(EntityType<? extends LostCrierEntity> entityType, World world) {
        super(entityType, world);

        ItemStack cryingObsidian = new ItemStack(Items.CRYING_OBSIDIAN);
        cryingObsidian.setCount(4);

        ItemStack chains = new ItemStack(Items.IRON_CHAIN);
        chains.setCount(2);

        ItemStack hardCryingObsidian = new ItemStack(Crying.HARD_CRYING_OBSIDIAN);
        hardCryingObsidian.setCount(1);

        inventory.addStack(cryingObsidian);
        inventory.addStack(chains);
        inventory.addStack(hardCryingObsidian);

        this.targetSelector.clear((target) -> {
            return true;
        });

        this.goalSelector.add(1, new WanderAroundGoal(this, 0.35d, 1, false));
        this.goalSelector.add(1, new LookAroundGoal(this));
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 2.0F));
    }

    public static DefaultAttributeContainer.Builder createLostCrierAttributes() {
        return PassiveEntity.createMobAttributes().add(EntityAttributes.MAX_HEALTH, 206d).add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.5d);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getEntityWorld().getDimensionEntry().getKey().orElse(Crying.CRYING_DIMENSION_TYPE) == DimensionTypes.OVERWORLD) {
            if (this.getEntityWorld() instanceof ServerWorld world) {
                Crying.CRIER.spawn(world, this.getBlockPos(), SpawnReason.NATURAL);
                this.getEntityWorld().playSound(null, this.getBlockPos(), Crying.CRIER_SCREAM_EVENT, SoundCategory.HOSTILE);
                this.discard();
            }
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getStackInHand(hand).getItem() == Crying.CRYING_CARROT) {
            this.heal(10.3F);
            player.getStackInHand(hand).decrementUnlessCreative(1, player);
            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }

    @Override
    public boolean isPanicking() {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public SimpleInventory getInventory() {
        return this.inventory;
    }

    @Override
    public boolean canPickupItem(ItemStack stack) {
        return false;
    }

    @Override
    protected void dropLoot(ServerWorld world, DamageSource damageSource, boolean causedByPlayer) {
        if (causedByPlayer) {
            if (world.getRandom().nextFloat() < 0.1F) {
                this.dropItem(world, Crying.HARDENED_CORE_PIECE);
            }
        }
    }
}
