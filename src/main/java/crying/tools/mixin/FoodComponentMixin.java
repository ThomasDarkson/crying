package crying.tools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.tools.Crying;
import crying.tools.interfaces.CryingInterface;
import crying.tools.interfaces.CryingManager;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(FoodComponent.class)
public abstract class FoodComponentMixin {
    @Inject(method = "onConsume", at = @At("TAIL"))
    public void onConsume(World world, LivingEntity user, ItemStack stack, ConsumableComponent consumable, CallbackInfo info) {
        if (user instanceof PlayerEntity player) {
            Item item = stack.getItem();
            if (item == Crying.crying_apple) {
                CryingManager manager = ((CryingInterface) (Object) player).getManagerOverride_crying();
                manager.decreaseLevel(-2F);
                manager.increasePermanentMaxLevel();
            }
        }
    }   
}
