package crying.mixin;

import crying.Crying;
import crying.interfaces.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionContents.class)
public abstract class WaterPotionMixin {
    @Inject(method = "onConsume", at = @At("TAIL"))
    public void onConsume(Level world, LivingEntity user, ItemStack stack, Consumable consumable, CallbackInfo info) {
        if (user instanceof Player player) {
            SanityManager manager = Crying.getSanityManager(player);
            PotionContents potion = (PotionContents) (Object) this;
            if (potion.is(Potions.WATER)) {
                manager.decreaseLevel(-20F);
                manager.setCollapseMultiplier(2);
            }
        }
    }
}
