package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.entities.GrapplingHookEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
    @Inject(method = "applyDrag", at = @At("TAIL"), cancellable = true)
    private void applyDrag(float drag, CallbackInfo info) { 
        PersistentProjectileEntity entity = (PersistentProjectileEntity) (Object) this;
        if (drag == 0.99F && entity instanceof GrapplingHookEntity) {
            info.cancel();
        }
    }
}
