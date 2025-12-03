package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
    @Inject(method = "registerDefaults", at = @At("TAIL"))
    private static void registerDefaults(BrewingRecipeRegistry.Builder builder, CallbackInfo info) {
        builder.registerPotionRecipe(Potions.AWKWARD, Crying.CRYING_RESIDUE, Crying.CRYING_POTION);
        builder.registerPotionRecipe(Crying.CRYING_POTION, Items.REDSTONE, Crying.LONG_CRYING_POTION);
    }
}
