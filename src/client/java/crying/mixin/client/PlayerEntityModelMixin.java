package crying.mixin.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerEntityModelMixin {
    @Inject(method = "setupAnim", at = @At("TAIL"))
    public void setAngles(AvatarRenderState playerEntityRenderState, CallbackInfo info) {
    }
}