package crying.tools.other;

import crying.tools.Crying;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public class CryingLoot {
    private static final Identifier BASTION_TREASURE = Identifier.ofVanilla("chests/bastion_treasure");
    private static final Identifier BASTION_BRIDGE = Identifier.ofVanilla("chests/bastion_bridge");

    private static final Identifier RUINED_PORTAL = Identifier.ofVanilla("chests/ruined_portal");

    private static final Identifier WOODLAND_MANSION = Identifier.ofVanilla("chests/woodland_mansion");

    private static final Identifier ANCIENT_CITY = Identifier.ofVanilla("chests/ancient_city");

    private static final Identifier REWARD_OMINOUS_RARE = Identifier.ofVanilla("chests/trial_chambers/reward_ominous_rare");
    private static final Identifier REWARD_OMINOUS_UNIQUE = Identifier.ofVanilla("chests/trial_chambers/reward_ominous_unique");

    private static final Identifier END_CITY_CHEST = Identifier.ofVanilla("chests/end_city_treasure");

    private static final Identifier STRONGHOLD_CORRIDOR = Identifier.ofVanilla("chests/stronghold_corridor");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            RegistryWrapper.Impl<Enchantment> enchantmentImpl = registries.getOrThrow(RegistryKeys.ENCHANTMENT);

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

            if (source.isBuiltin() && RUINED_PORTAL.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1F))
                    .with(ItemEntry.builder(Items.CRYING_OBSIDIAN))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1F, 4F)))
                    .conditionally(RandomChanceLootCondition.builder(0.66F));

                tableBuilder.pool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(6F))
                    .with(ItemEntry.builder(Crying.residue))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1F, 2F)))
                    .conditionally(RandomChanceLootCondition.builder(0.03F));

                tableBuilder.pool(poolBuilder2);
            }

            if (source.isBuiltin() && REWARD_OMINOUS_RARE.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(2F))
                    .with(ItemEntry.builder(Crying.hoe))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1F, 1F)))
                    .conditionally(RandomChanceLootCondition.builder(0.08F));

                tableBuilder.pool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1F))
                    .with(ItemEntry.builder(Crying.axe))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1F, 1F)))
                    .conditionally(RandomChanceLootCondition.builder(0.04F));

                tableBuilder.pool(poolBuilder2);

                LootPool.Builder poolBuilder3 = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1F))
                    .with(ItemEntry.builder(Crying.pickaxe))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1F, 1F)))
                    .conditionally(RandomChanceLootCondition.builder(0.02F));

                tableBuilder.pool(poolBuilder3);
            }

            if (source.isBuiltin() && REWARD_OMINOUS_UNIQUE.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BOOK)
                        .apply(EnchantRandomlyLootFunction.builder(registries).options(enchantmentImpl.getOrThrow(CryingEnchantmentTags.FEATHERED_LOOT)))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.04f)));

                tableBuilder.pool(poolBuilder);
            }

            if (source.isBuiltin() && WOODLAND_MANSION.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BOOK)
                        .apply(EnchantRandomlyLootFunction.builder(registries).options(enchantmentImpl.getOrThrow(CryingEnchantmentTags.ALL_LOOT)))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.15f)));

                tableBuilder.pool(poolBuilder);
            }

            if (source.isBuiltin() && ANCIENT_CITY.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BOOK)
                        .apply(EnchantRandomlyLootFunction.builder(registries).options(enchantmentImpl.getOrThrow(CryingEnchantmentTags.AEGIS_LOOT)))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.66f)));

                tableBuilder.pool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BOOK)
                        .apply(EnchantRandomlyLootFunction.builder(registries).options(enchantmentImpl.getOrThrow(CryingEnchantmentTags.BLOODLUST_LOOT)))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.15f)));

                tableBuilder.pool(poolBuilder2);
            }

            if (source.isBuiltin() && END_CITY_CHEST.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BOOK)
                        .apply(EnchantRandomlyLootFunction.builder(registries).options(enchantmentImpl.getOrThrow(CryingEnchantmentTags.FEATHERED_LOOT)))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(1f)));

                tableBuilder.pool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BOOK)
                        .apply(EnchantRandomlyLootFunction.builder(registries).options(enchantmentImpl.getOrThrow(CryingEnchantmentTags.AEGIS_LOOT)))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.5f)));

                tableBuilder.pool(poolBuilder2);

                LootPool.Builder poolBuilder3 = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BOOK)
                        .apply(EnchantRandomlyLootFunction.builder(registries).options(enchantmentImpl.getOrThrow(CryingEnchantmentTags.BLOODLUST_LOOT)))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.25f)));

                tableBuilder.pool(poolBuilder3);
            }

            if (source.isBuiltin() && STRONGHOLD_CORRIDOR.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BOOK)
                        .apply(EnchantRandomlyLootFunction.builder(registries).options(enchantmentImpl.getOrThrow(CryingEnchantmentTags.FEATHERED_LOOT)))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.25f)));

                tableBuilder.pool(poolBuilder);

                LootPool.Builder poolBuilder2 = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BOOK)
                        .apply(EnchantRandomlyLootFunction.builder(registries).options(enchantmentImpl.getOrThrow(CryingEnchantmentTags.AEGIS_LOOT)))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.175f)));

                tableBuilder.pool(poolBuilder2);

                LootPool.Builder poolBuilder3 = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BOOK)
                        .apply(EnchantRandomlyLootFunction.builder(registries).options(enchantmentImpl.getOrThrow(CryingEnchantmentTags.BLOODLUST_LOOT)))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(1f)));

                tableBuilder.pool(poolBuilder3);
            }
        });
    }
}