package crying.tools.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.tools.entities.CryingCatEntity;
import crying.tools.interfaces.SanityInterface;
import crying.tools.interfaces.SanityManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(TameableEntity.class)
public class TameableEntityMixin {
    @Inject(method = "setOwner", at = @At("TAIL"))
    public void setOwner(@Nullable LivingEntity entity, CallbackInfo info) {
        TameableEntity tameable = (TameableEntity) (Object) this;
        if (!(tameable instanceof CryingCatEntity) && (entity instanceof PlayerEntity player)) {
            SanityManager manager = ((SanityInterface) (Object) player).getManagerOverride_crying();
            manager.decreaseLevel(-20F);
        }
    }
}
