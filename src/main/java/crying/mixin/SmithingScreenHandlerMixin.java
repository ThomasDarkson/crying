package crying.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.Slot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingScreenHandler.class)
public class SmithingScreenHandlerMixin {
    @Inject(method = "canInsertIntoSlot", at = @At("HEAD"), cancellable = true)
    private void allowNetheriteInSecondSlot(ItemStack stack, Slot slot, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(true);
    }
}