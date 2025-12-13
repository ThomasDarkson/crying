package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.entities.GrapplingHookEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;

@Mixin(AbstractArrow.class)
public class PersistentProjectileEntityMixin {
    @Inject(method = "applyInertia", at = @At("TAIL"), cancellable = true)
    private void applyDrag(float drag, CallbackInfo info) { 
        AbstractArrow entity = (AbstractArrow) (Object) this;
        if (drag == 0.99F && entity instanceof GrapplingHookEntity) {
            info.cancel();
        }
    }
}
