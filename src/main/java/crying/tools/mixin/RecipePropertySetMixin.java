package crying.tools.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipePropertySet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.tools.Crying;

@Mixin(RecipePropertySet.class)
public abstract class RecipePropertySetMixin {
    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void allowNetherite(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        for (Item i : Crying.itemsAllowed)
        {
            if (stack.isOf(i)) {
                info.setReturnValue(true); 
                break;
            }
        }
    }
}