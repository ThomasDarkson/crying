package crying.tools.other;

import java.util.concurrent.CompletableFuture;

import crying.tools.Crying;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CryingEnchantmentTags extends FabricTagProvider<Enchantment> {
    public static final TagKey<Enchantment> FEATHERED_LOOT = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Crying.ID, "feathered_loot"));

    public static final TagKey<Enchantment> AEGIS_LOOT = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Crying.ID, "aegis_loot"));

    public static final TagKey<Enchantment> BLOODLUST_LOOT = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Crying.ID, "bloodlust_loot"));

    public static final TagKey<Enchantment> ALL_LOOT = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Crying.ID, "all_loot"));

    public CryingEnchantmentTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
    }

    public static void initialize() {

    }

    @Override
    protected void configure(WrapperLookup wrapperLookup) {
    }
}