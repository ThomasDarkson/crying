package crying.tools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.tools.entities.CryingCatEntity;
import crying.tools.interfaces.SanityInterface;
import crying.tools.interfaces.SanityManager;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(TameableEntity.class)
public class TameableEntityMixin {
    @Inject(method = "setOwner", at = @At("TAIL"))
    public void setOwner(PlayerEntity player, CallbackInfo info) {
        TameableEntity entity = (TameableEntity) (Object) this;
        if (!(entity instanceof CryingCatEntity)) {
            SanityManager manager = ((SanityInterface) (Object) player).getManagerOverride_crying();
            manager.decreaseLevel(-20F);
        }
    }
}
