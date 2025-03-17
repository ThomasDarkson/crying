package crying.tools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.tools.Crying;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo info) {
        ItemEntity item = (ItemEntity) (Object) this;
        ItemStack stack = item.getStack();
        if (stack != null && stack.getItem() == Crying.crying_cat_item) {
            item.setNeverDespawn();
        }
    }
}
