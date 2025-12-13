package crying.entities;

import crying.Crying;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

public class LostCrierEntity extends PathfinderMob implements InventoryCarrier {
    SimpleContainer inventory = new SimpleContainer(4);
    
    public LostCrierEntity(EntityType<? extends LostCrierEntity> entityType, Level world) {
        super(entityType, world);

        ItemStack cryingObsidian = new ItemStack(Items.CRYING_OBSIDIAN);
        cryingObsidian.setCount(4);

        ItemStack chains = new ItemStack(Items.CHAIN);
        chains.setCount(2);

        ItemStack hardCryingObsidian = new ItemStack(Crying.HARD_CRYING_OBSIDIAN);
        hardCryingObsidian.setCount(1);

        inventory.addItem(cryingObsidian);
        inventory.addItem(chains);
        inventory.addItem(hardCryingObsidian);

        this.targetSelector.removeAllGoals((target) -> {
            return true;
        });

        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 0.35d, 1, false));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 2.0F));
    }

    public static AttributeSupplier.Builder createLostCrierAttributes() {
        return AgeableMob.createMobAttributes().add(Attributes.MAX_HEALTH, 206d).add(Attributes.KNOCKBACK_RESISTANCE, 0.5d);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().dimensionTypeRegistration().unwrapKey().orElse(Crying.CRYING_DIMENSION_TYPE) == BuiltinDimensionTypes.OVERWORLD) {
            if (this.level() instanceof ServerLevel world) {
                Crying.CRIER.spawn(world, this.blockPosition(), EntitySpawnReason.NATURAL);
                this.level().playSound(null, this.blockPosition(), Crying.CRIER_SCREAM_EVENT, SoundSource.HOSTILE);
                this.discard();
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).getItem() == Crying.CRYING_CARROT) {
            this.heal(10.3F);
            player.getItemInHand(hand).consume(1, player);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
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
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return false;
    }

    @Override
    protected void dropFromLootTable(ServerLevel world, DamageSource damageSource, boolean causedByPlayer) {
        if (causedByPlayer) {
            if (world.getRandom().nextFloat() < 0.1F) {
                this.spawnAtLocation(world, Crying.HARDENED_CORE_PIECE);
            }
        }
    }
}
