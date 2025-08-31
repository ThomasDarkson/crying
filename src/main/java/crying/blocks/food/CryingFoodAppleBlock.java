package crying.blocks.food;

import crying.Crying;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class CryingFoodAppleBlock extends CryingFoodBlock {
    public CryingFoodAppleBlock() {
        super(Settings.create().
            registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Crying.ID, "crying_food_apple"))).
            strength(0F, 36000000.0F).
            luminance((state) -> {
                return 11;
            }), "crying_food_apple");
    }

    @Override
    public ItemStack stack() {
        return new ItemStack(Crying.CRYING_APPLE);
    }
}
