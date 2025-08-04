package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.CryingClient;
import crying.PlayerEntityRenderStateVarsInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin {
    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    protected void renderItem(ArmedEntityRenderState entityState, ItemRenderState itemState, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if (entityState instanceof PlayerEntityRenderState playerState) {
            PlayerEntityRenderStateVarsInterface betterState = ((PlayerEntityRenderStateVarsInterface) (Object) playerState);
            Hand hand = betterState.get_cryingShieldHand();
            if (hand != null && CryingClient.compareHandtoArm(hand, arm, playerState.mainArm)) {
                matrices.push();
                float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false);
                float age = betterState.get_realAge() + tickDelta;

                float offset = (float) Math.sin(age / 8.0f) * 0.1f;
                matrices.translate(0.0F, 0.2F + offset, 0.0F);

                float spin = (age * 4f) % 360f;
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(spin));

                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-105.0F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                boolean bl = arm == Arm.LEFT;
                matrices.translate((float)(bl ? -1 : 1) / 16.0F, 0.125F, -0.625F);
                if (bl)
                    matrices.translate(-0.35d, 0d, 0d);
                else
                    matrices.translate(0.35d, 0d, 0d);
                    
                itemState.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
                matrices.pop();
                info.cancel();
            }
        }
    }
}
