package crying.other;

import crying.Crying;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class CryingLoot {
    private static final Identifier BASTION_TREASURE = Identifier.ofVanilla("chests/bastion_treasure");
    private static final Identifier BASTION_BRIDGE = Identifier.ofVanilla("chests/bastion_bridge");

    private static final Identifier RUINED_PORTAL = Identifier.ofVanilla("chests/ruined_portal");

    private static final Identifier REWARD_OMINOUS_RARE = Identifier.ofVanilla("chests/trial_chambers/reward_ominous_rare");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin() && BASTION_TREASURE.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Crying.HARD_CRYING_OBSIDIAN.asItem())
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.05f)));

                tableBuilder.pool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(2.0F))
                        .with(ItemEntry.builder(Crying.CRYING_INGOT)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.1125f)));

                tableBuilder.pool(poolBuilder2);
            }

            if (source.isBuiltin() && BASTION_BRIDGE.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(6.0F))
                        .with(ItemEntry.builder(Crying.HARD_CRYING_OBSIDIAN.asItem())
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.0103f)));

                tableBuilder.pool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(2.0F))
                        .with(ItemEntry.builder(Crying.CRYING_INGOT)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.1f)));

                tableBuilder.pool(poolBuilder2);
            }

            if (source.isBuiltin() && RUINED_PORTAL.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1F))
                    .with(ItemEntry.builder(Items.CRYING_OBSIDIAN))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1F, 4F)))
                    .conditionally(RandomChanceLootCondition.builder(0.66F));

                tableBuilder.pool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(6F))
                    .with(ItemEntry.builder(Crying.CRYING_RESIDUE))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1F, 2F)))
                    .conditionally(RandomChanceLootCondition.builder(0.03F));

                tableBuilder.pool(poolBuilder2);
            }

            if (source.isBuiltin() && REWARD_OMINOUS_RARE.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(2F))
                    .with(ItemEntry.builder(Crying.CRYING_HOE_DIAMOND))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1F, 1F)))
                    .conditionally(RandomChanceLootCondition.builder(0.08F));

                tableBuilder.pool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1F))
                    .with(ItemEntry.builder(Crying.CRYING_AXE_IRON))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1F, 1F)))
                    .conditionally(RandomChanceLootCondition.builder(0.04F));

                tableBuilder.pool(poolBuilder2);

                LootPool.Builder poolBuilder3 = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1F))
                    .with(ItemEntry.builder(Crying.CRYING_PICKAXE_GOLD))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1F, 1F)))
                    .conditionally(RandomChanceLootCondition.builder(0.02F));

                tableBuilder.pool(poolBuilder3);
            }
        });
    }
}