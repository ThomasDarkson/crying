package crying.blocks.food;

import crying.Crying;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CryingFoodAppleBlock extends CryingFoodBlock {
    public CryingFoodAppleBlock() {
        super(Properties.of().
            setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_food_apple"))).
            strength(0F, 36000000.0F).
            lightLevel((state) -> {
                return 11;
            }), "crying_food_apple");
    }

    @Override
    public ItemStack stack() {
        return new ItemStack(Crying.CRYING_APPLE);
    }
}
