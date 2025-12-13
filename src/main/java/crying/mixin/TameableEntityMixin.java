package crying.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import crying.interfaces.SanityManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;

@Mixin(TamableAnimal.class)
public class TameableEntityMixin {
    @Inject(method = "setOwner", at = @At("TAIL"))
    public void setOwner(@Nullable LivingEntity entity, CallbackInfo info) {
        if ((entity instanceof Player player)) {
            SanityManager manager = Crying.getSanityManager(player);
            manager.decreaseLevel(-20F);
        }
    }
}
