package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin {
    @Inject(method = "setAngles", at = @At("TAIL"))
    public void setAngles(PlayerEntityRenderState playerEntityRenderState, CallbackInfo info) {
    }
}