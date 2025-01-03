package crying.tools.mixin;

import crying.tools.interfaces.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.world.World;

@Mixin(PotionContentsComponent.class)
public abstract class WaterPotionMixin {
    @Inject(method = "onConsume", at = @At("TAIL"))
    public void onConsume(World world, LivingEntity user, ItemStack stack, ConsumableComponent consumable, CallbackInfo info) {
        if (user instanceof PlayerEntity player) {
            CryingManager manager = ((CryingInterface) (Object) player).getManagerOverride_crying();
            PotionContentsComponent potion = (PotionContentsComponent) (Object) this;
            if (potion.matches(Potions.WATER)) {
                manager.decreaseLevel(-2F);
                manager.setRegen(true);
            }
        }
    }
}
