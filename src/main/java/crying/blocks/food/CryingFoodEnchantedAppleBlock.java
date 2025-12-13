package crying.blocks.food;

import crying.Crying;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CryingFoodEnchantedAppleBlock extends CryingFoodBlock {
    public CryingFoodEnchantedAppleBlock() {
        super(Properties.of().
            setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_food_enchanted_apple"))).
            strength(0F, 36000000.0F).
            lightLevel((state) -> {
                return 11;
            }), "crying_food_enchanted_apple");
    }

    @Override
    public ItemStack stack() {
        return new ItemStack(Crying.ENCHANTED_CRYING_APPLE);
    }
}
