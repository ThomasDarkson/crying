package crying.enchantments;

import crying.Crying;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class BaneOfCriers {
    public static final ResourceKey<Enchantment> bane_of_criers = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Crying.ID, "bane_of_criers"));

    public static void initialize() {
    }   
}