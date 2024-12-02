package crying.tools.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.Slot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.tools.CryingTools;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler {

    protected SmithingScreenHandlerMixin(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerType.SMITHING, syncId, playerInventory, ScreenHandlerContext.EMPTY, null);
    }

    @Inject(method = "canInsertIntoSlot", at = @At("HEAD"), cancellable = true)
    private void allowNetheriteInSecondSlot(ItemStack stack, Slot slot, CallbackInfoReturnable<Boolean> info) {
        for (Item i : CryingTools.itemsAllowed)
        {
            if (slot.id == SmithingScreenHandler.EQUIPMENT_ID && stack.isOf(i)) {
                info.setReturnValue(true);
                break;
            }
        }
    }
}