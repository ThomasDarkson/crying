package crying.tools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.tools.Crying;
import crying.tools.interfaces.SanityInterface;
import crying.tools.interfaces.SanityManager;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

@Mixin(FoodComponent.class)
public abstract class FoodComponentMixin {
    @Inject(method = "onConsume", at = @At("TAIL"))
    public void onConsume(World world, LivingEntity user, ItemStack stack, ConsumableComponent consumable, CallbackInfo info) {
        if (user instanceof PlayerEntity player) {
            Item item = stack.getItem();
            SanityManager manager = ((SanityInterface) (Object) player).getManagerOverride_crying();
            if (item == Crying.crying_apple) {
                manager.decreaseLevel(-2F);
                manager.increasePermanentMaxLevel();
            }
            else if (
                item == Items.PUFFERFISH ||
                item == Items.ROTTEN_FLESH ||
                item == Items.POISONOUS_POTATO ||
                item == Items.CHICKEN ||
                item == Items.SPIDER_EYE
            ) {
                manager.decreaseLevel(item == Items.PUFFERFISH ? 20F : 6F);
            }
            else if (item == Items.HONEY_BOTTLE) {
                manager.decreaseLevel(-20F);
            }
            else if (
                item == Items.COOKED_BEEF ||
                item == Items.COOKED_PORKCHOP ||
                item == Items.COOKED_MUTTON
            ) {
                if (world.getRandom().nextFloat() < 0.6F) {
                    manager.decreaseLevel(-4F);
                }
            }
        }
    }   
}
