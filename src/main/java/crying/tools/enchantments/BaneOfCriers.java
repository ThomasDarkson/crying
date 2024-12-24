package crying.tools.enchantments;

import crying.tools.Crying;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class BaneOfCriers {
    public static final RegistryKey<Enchantment> bane_of_criers = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Crying.MOD_ID, "bane_of_criers"));

    public static void initialize() {

    }   
}