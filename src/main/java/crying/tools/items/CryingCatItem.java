package crying.tools.items;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import crying.tools.Crying;
import crying.tools.entities.CryingCatEntity;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.Spawner;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.util.hit.HitResult.Type;

public class CryingCatItem extends SpawnEggItem {
    private static int defense = 6;

    public CryingCatItem() {
        super(Crying.CRYING_CAT, new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_cat_item")))
            .attributeModifiers(createAttributeModifiers(EquipmentType.HELMET))
            .component(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(EquipmentType.HELMET.getEquipmentSlot()).equipSound(RegistryEntry.of(SoundEvents.ENTITY_CAT_AMBIENT)).build())
            .maxCount(1)
            .rarity(Rarity.EPIC)
            .fireproof());

        Crying.register(this, "crying_cat_item");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register((itemGroup) -> itemGroup.add(this));
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (slot != null)
        {
            if (slot.equals(EquipmentSlot.MAINHAND)) {
                if (world.getRandom().nextFloat() < 0.02F) {
                    world.playSound((PlayerEntity) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_CAT_PURR, entity.getSoundCategory());
                }
            }
            else if (entity instanceof PlayerEntity) {
                if (slot.equals(EquipmentSlot.HEAD)) {
                    if (world.getRandom().nextFloat() < 0.0025F) 
                        world.playSound((PlayerEntity) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_CAT_PURR, entity.getSoundCategory());
                }
            }
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            ItemStack itemStack = context.getStack();
            BlockPos blockPos = context.getBlockPos();
            Direction direction = context.getSide();
            BlockState blockState = world.getBlockState(blockPos);
            BlockEntity var8 = world.getBlockEntity(blockPos);
            @SuppressWarnings("rawtypes")
            EntityType entityType;
            if (var8 instanceof Spawner) {
                return ActionResult.PASS;
            } else {
                BlockPos blockPos2;
                if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
                    blockPos2 = blockPos;
                } else {
                    blockPos2 = blockPos.offset(direction);
                }

                entityType = this.getEntityType(world.getRegistryManager(), itemStack);
                Entity entity = entityType.spawnFromItemStack((ServerWorld)world, itemStack, context.getPlayer(), blockPos2, SpawnReason.SPAWN_ITEM_USE, true, !Objects.equals(blockPos, blockPos2) && direction == Direction.UP);
                if (entity != null) {
                    itemStack.decrement(1);
                    world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);

                    if (entity instanceof CryingCatEntity cat) {
                        cat.setOgOwner(context.getPlayer());
                        cat.setOwner(context.getPlayer());
                        cat.setTamedBy(context.getPlayer());
                        cat.setPersistent();
                    }
                }

                return ActionResult.SUCCESS;
            }
        }
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult blockHitResult = raycast(world, user, FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() != Type.BLOCK) {
            return ActionResult.PASS;
        } else if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)world;
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!(world.getBlockState(blockPos).getBlock() instanceof FluidBlock)) {
                return ActionResult.PASS;
            } else if (world.canEntityModifyAt(user, blockPos) && user.canPlaceOn(blockPos, blockHitResult.getSide(), itemStack)) {
                EntityType<?> entityType = this.getEntityType(serverWorld.getRegistryManager(), itemStack);
                Entity entity = entityType.spawnFromItemStack(serverWorld, itemStack, user, blockPos, SpawnReason.SPAWN_ITEM_USE, false, false);
                if (entity == null) {
                    return ActionResult.PASS;
                } else {
                    itemStack.decrementUnlessCreative(1, user);
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                    world.emitGameEvent(user, GameEvent.ENTITY_PLACE, entity.getPos());

                    if (entity instanceof CryingCatEntity cat) {
                        cat.setOgOwner(user);
                        cat.setOwner(user);
                        cat.setTamedBy(user);
                        cat.setPersistent();
                    }
                    return ActionResult.SUCCESS;
                }
            } else {
                return ActionResult.FAIL;
            }
        } else {
            return ActionResult.SUCCESS;
        }
    }

    private static AttributeModifiersComponent createAttributeModifiers(EquipmentType equipmentType) {
        int i = defense;
        AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
        AttributeModifierSlot attributeModifierSlot = AttributeModifierSlot.forEquipmentSlot(equipmentType.getEquipmentSlot());
        Identifier identifier = Identifier.ofVanilla("armor." + equipmentType.getName());

        builder.add(EntityAttributes.ARMOR, new EntityAttributeModifier(identifier, (double) i, Operation.ADD_VALUE), attributeModifierSlot);
        return builder.build();
    }
}
