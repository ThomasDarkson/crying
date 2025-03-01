package crying.tools.tools;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import crying.tools.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.ToolComponent.Rule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.GameEvent.Emitter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

public class TheCryingBeing extends Item {

    static Map<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> TILLING_ACTIONS = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT_PATH, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.COARSE_DIRT, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.DIRT.getDefaultState())), Blocks.ROOTED_DIRT, Pair.of((itemUsageContext) -> {
        return true;
    }, createTillAndDropAction(Blocks.DIRT.getDefaultState(), Items.HANGING_ROOTS))));

    public TheCryingBeing() {
        super(
            new Item.Settings()
            .rarity(Rarity.EPIC)
            .fireproof()
            .maxDamage(Integer.MAX_VALUE)
            .component(DataComponentTypes.TOOL, TheCryingBeing.createToolComponent())
            .attributeModifiers(TheCryingBeing.createAttributeModifiers())
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "the_crying_being")))
        );

        Crying.register(this, "the_crying_being");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.MACE, this));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Random r = new Random();
        int low = 0;
        int high = 5;
        int result = r.nextInt(high - low) + low;

        String[] literals = {
            "618",
            "...",
            "V2h5Pw==",
            "MTE=",
            "TGl2aW5nIE1pY2U=",
            ""
        };

        tooltip.add(Text.literal(literals[result]).fillStyle(Style.EMPTY.withObfuscated(true)));
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder().add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, ((double) Integer.MAX_VALUE - 1), Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, 662F, Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
    }

    public static ToolComponent createToolComponent() {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        return new ToolComponent(List.of(Rule.ofAlwaysDropping(registryEntryLookup.getOrThrow(TagKey.of(RegistryKeys.BLOCK, Identifier.of(Crying.ID, "mineable/the_crying_being"))), 2147483647F)), 1.0F, 1);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (shouldDealAdditionalDamage(attacker)) {
            ServerWorld serverWorld = (ServerWorld) attacker.getWorld();
            attacker.setVelocity(attacker.getVelocity().withAxis(Axis.Y, 0.009999999776482582));
            ServerPlayerEntity serverPlayerEntity;
            if (attacker instanceof ServerPlayerEntity) {
                serverPlayerEntity = (ServerPlayerEntity) attacker;
                serverPlayerEntity.currentExplosionImpactPos = this.getCurrentExplosionImpactPos(serverPlayerEntity);
                serverPlayerEntity.setIgnoreFallDamageFromCurrentExplosion(true);
                serverPlayerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayerEntity));
            }

            if (target.isOnGround()) {
                if (attacker instanceof ServerPlayerEntity) {
                    serverPlayerEntity = (ServerPlayerEntity) attacker;
                    serverPlayerEntity.setSpawnExtraParticlesOnFall(true);
                }

                SoundEvent soundEvent = attacker.fallDistance > 5.0F ? SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY : SoundEvents.ITEM_MACE_SMASH_GROUND;
                serverWorld.playSound((PlayerEntity) null, attacker.getX(), attacker.getY(), attacker.getZ(), soundEvent, attacker.getSoundCategory(), 1.0F, 1.0F);
            } else {
                serverWorld.playSound((PlayerEntity) null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.ITEM_MACE_SMASH_AIR, attacker.getSoundCategory(), 1.0F, 1.0F);
            }

            knockbackNearbyEntities(serverWorld, attacker, target);
        }

        return true;
    }

    private Vec3d getCurrentExplosionImpactPos(ServerPlayerEntity player) {
        return player.shouldIgnoreFallDamageFromCurrentExplosion() && player.currentExplosionImpactPos != null && player.currentExplosionImpactPos.y <= player.getPos().y ? player.currentExplosionImpactPos : player.getPos();
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }


    private static void knockbackNearbyEntities(World world, Entity attacker, Entity attacked) {
        world.syncWorldEvent(2013, attacked.getSteppingPos(), 750);
        world.getEntitiesByClass(LivingEntity.class, attacked.getBoundingBox().expand(3.5), getKnockbackPredicate(attacker, attacked)).forEach((entity) -> {
            Vec3d vec3d = entity.getPos().subtract(attacked.getPos());
            double d = getKnockback(attacker, entity, vec3d);
            Vec3d vec3d2 = vec3d.normalize().multiply(d);
            if (d > 0.0) {
                entity.addVelocity(vec3d2.x, 0.699999988079071, vec3d2.z);
                if (entity instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
                    serverPlayerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayerEntity));
                }
            }

        });
    }

    private static Predicate<LivingEntity> getKnockbackPredicate(Entity attacker, Entity attacked) {
        return (entity) -> {
            boolean var10000;
            boolean bl;
            boolean bl2;
            boolean bl3;
            label62: {
                bl = !entity.isSpectator();
                bl2 = entity != attacker && entity != attacked;
                bl3 = !attacker.isTeammate(entity);
                if (entity instanceof TameableEntity tameableEntity) {
                    if (tameableEntity.isTamed() && attacker.getUuid().equals(tameableEntity.getOwnerUuid())) {
                        var10000 = true;
                        break label62;
                    }
                }

                var10000 = false;
            }

            boolean bl4;
            label55: {
                bl4 = !var10000;
                if (entity instanceof ArmorStandEntity armorStandEntity) {
                    if (armorStandEntity.isMarker()) {
                        var10000 = false;
                        break label55;
                    }
                }

                var10000 = true;
            }

            boolean bl5 = var10000;
            boolean bl6 = attacked.squaredDistanceTo(entity) <= Math.pow(3.5, 2.0);
            return bl && bl2 && bl3 && bl4 && bl5 && bl6;
        };
    }

    private static double getKnockback(Entity attacker, LivingEntity attacked, Vec3d distance) {
        return (3.5 - distance.length()) * 0.699999988079071 * (double) (attacker.fallDistance > 5.0F ? 2 : 1) * (1.0 - attacked.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
    }

    public static boolean shouldDealAdditionalDamage(LivingEntity attacker) {
        return attacker.fallDistance > 1.5F && !attacker.isGliding();
    }

    @Override
    @Nullable
    public DamageSource getDamageSource(LivingEntity user) {
        return shouldDealAdditionalDamage(user) ? user.getDamageSources().maceSmash(user) : super.getDamageSource(user);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair = (Pair)TILLING_ACTIONS.get(world.getBlockState(blockPos).getBlock());
        if (pair == null) {
            return ActionResult.PASS;
        } else {
            Predicate<ItemUsageContext> predicate = (Predicate)pair.getFirst();
            Consumer<ItemUsageContext> consumer = (Consumer)pair.getSecond();
            if (predicate.test(context)) {
                PlayerEntity playerEntity = context.getPlayer();
                world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClient) {
                consumer.accept(context);
                if (playerEntity != null) {
                    context.getStack().damage(1, playerEntity, LivingEntity.getSlotForHand(context.getHand()));
                }
                }

                return ActionResult.SUCCESS;
            } else {
                return ActionResult.PASS;
            }
        }
    }

    public static Consumer<ItemUsageContext> createTillAction(BlockState result) {
        return (context) -> {
            context.getWorld().setBlockState(context.getBlockPos(), result, 11);
            context.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, context.getBlockPos(), Emitter.of(context.getPlayer(), result));
        };
    }

    public static Consumer<ItemUsageContext> createTillAndDropAction(BlockState result, ItemConvertible droppedItem) {
        return (context) -> {
            context.getWorld().setBlockState(context.getBlockPos(), result, 11);
            context.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, context.getBlockPos(), Emitter.of(context.getPlayer(), result));
            Block.dropStack(context.getWorld(), context.getBlockPos(), context.getSide(), new ItemStack(droppedItem));
        };
    }

    public static boolean canTillFarmland(ItemUsageContext context) {
        return context.getSide() != Direction.DOWN && context.getWorld().getBlockState(context.getBlockPos().up()).isAir();
    }
}