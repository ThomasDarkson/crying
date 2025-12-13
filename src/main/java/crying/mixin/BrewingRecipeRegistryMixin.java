package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

@Mixin(PotionBrewing.class)
public class BrewingRecipeRegistryMixin {
    @Inject(method = "addVanillaMixes", at = @At("TAIL"))
    private static void registerDefaults(PotionBrewing.Builder builder, CallbackInfo info) {
        builder.addMix(Potions.AWKWARD, Crying.CRYING_RESIDUE, Crying.CRYING_POTION);
        builder.addMix(Crying.CRYING_POTION, Items.REDSTONE, Crying.LONG_CRYING_POTION);
    }
}
