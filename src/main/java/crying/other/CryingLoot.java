package crying.other;

import crying.Crying;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class CryingLoot {
    private static final ResourceLocation BASTION_TREASURE = ResourceLocation.withDefaultNamespace("chests/bastion_treasure");
    private static final ResourceLocation BASTION_BRIDGE = ResourceLocation.withDefaultNamespace("chests/bastion_bridge");

    private static final ResourceLocation RUINED_PORTAL = ResourceLocation.withDefaultNamespace("chests/ruined_portal");

    private static final ResourceLocation REWARD_OMINOUS_RARE = ResourceLocation.withDefaultNamespace("chests/trial_chambers/reward_ominous_rare");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin() && BASTION_TREASURE.equals(key.location())) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Crying.HARD_CRYING_OBSIDIAN.asItem())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 1.0F)))
                        .when(LootItemRandomChanceCondition.randomChance(0.05f)));

                tableBuilder.withPool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(2.0F))
                        .add(LootItem.lootTableItem(Crying.CRYING_INGOT)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        .when(LootItemRandomChanceCondition.randomChance(0.1125f)));

                tableBuilder.withPool(poolBuilder2);
            }

            if (source.isBuiltin() && BASTION_BRIDGE.equals(key.location())) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(6.0F))
                        .add(LootItem.lootTableItem(Crying.HARD_CRYING_OBSIDIAN.asItem())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 1.0F)))
                        .when(LootItemRandomChanceCondition.randomChance(0.0103f)));

                tableBuilder.withPool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(2.0F))
                        .add(LootItem.lootTableItem(Crying.CRYING_INGOT)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 1.0F)))
                        .when(LootItemRandomChanceCondition.randomChance(0.1f)));

                tableBuilder.withPool(poolBuilder2);
            }

            if (source.isBuiltin() && RUINED_PORTAL.equals(key.location())) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1F))
                    .add(LootItem.lootTableItem(Items.CRYING_OBSIDIAN))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1F, 4F)))
                    .when(LootItemRandomChanceCondition.randomChance(0.66F));

                tableBuilder.withPool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(6F))
                    .add(LootItem.lootTableItem(Crying.CRYING_RESIDUE))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1F, 2F)))
                    .when(LootItemRandomChanceCondition.randomChance(0.03F));

                tableBuilder.withPool(poolBuilder2);
            }

            if (source.isBuiltin() && REWARD_OMINOUS_RARE.equals(key.location())) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(2F))
                    .add(LootItem.lootTableItem(Crying.CRYING_HOE_DIAMOND))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1F, 1F)))
                    .when(LootItemRandomChanceCondition.randomChance(0.08F));

                tableBuilder.withPool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1F))
                    .add(LootItem.lootTableItem(Crying.CRYING_AXE_IRON))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1F, 1F)))
                    .when(LootItemRandomChanceCondition.randomChance(0.04F));

                tableBuilder.withPool(poolBuilder2);

                LootPool.Builder poolBuilder3 = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1F))
                    .add(LootItem.lootTableItem(Crying.CRYING_PICKAXE_GOLD))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1F, 1F)))
                    .when(LootItemRandomChanceCondition.randomChance(0.02F));

                tableBuilder.withPool(poolBuilder3);
            }
        });
    }
}