package crying.tools.enchantments;

import crying.tools.Crying;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class Feathered {
    public static final RegistryKey<Enchantment> FEATHERED = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Crying.ID, "feathered"));

    public static void initialize() {
        
    }
}
