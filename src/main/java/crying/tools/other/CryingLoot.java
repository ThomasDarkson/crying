package crying.tools.other;

import crying.tools.Crying;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class CryingLoot {
    private static final Identifier BASTION_TREASURE = Identifier.of("minecraft", "chests/bastion_treasure");
    private static final Identifier BASTION_BRIDGE = Identifier.of("minecraft", "chests/bastion_bridge");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin() && BASTION_TREASURE.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Crying.upgrade)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.05f)));

                tableBuilder.pool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(2.0F))
                        .with(ItemEntry.builder(Crying.ingot)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.1125f)));

                tableBuilder.pool(poolBuilder2);
            }

            if (source.isBuiltin() && BASTION_BRIDGE.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(6.0F))
                        .with(ItemEntry.builder(Crying.upgrade)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.0103f)));

                tableBuilder.pool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(2.0F))
                        .with(ItemEntry.builder(Crying.ingot)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.1f)));

                tableBuilder.pool(poolBuilder2);
            }
        });
    }
}