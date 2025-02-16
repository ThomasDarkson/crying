package crying.tools.enchantments;

import crying.tools.Crying;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class Smoothness {
    public static final RegistryKey<Enchantment> smoothness = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Crying.MOD_ID, "smoothness"));

    public static void initialize() {

    }   
}