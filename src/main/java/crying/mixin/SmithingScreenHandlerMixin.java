package crying.mixin;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingMenu.class)
public class SmithingScreenHandlerMixin {
    @Inject(method = "canTakeItemForPickAll", at = @At("HEAD"), cancellable = true)
    private void allowNetheriteInSecondSlot(ItemStack stack, Slot slot, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(true);
    }
}