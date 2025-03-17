package crying.tools.enchantments;

import crying.tools.Crying;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class Aegis {
    public static final RegistryKey<Enchantment> AEGIS = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Crying.ID, "aegis"));

    public static void initialize() {
        
    }
}
