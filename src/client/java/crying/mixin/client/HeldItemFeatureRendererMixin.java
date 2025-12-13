package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import crying.CryingClient;
import crying.interfaces.PlayerEntityRenderStateVarsInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

@Mixin(ItemInHandLayer.class)
public class HeldItemFeatureRendererMixin {
    @Inject(method = "submitArmWithItem", at = @At("HEAD"), cancellable = true)
    protected void renderItem(ArmedEntityRenderState entityState, ItemStackRenderState itemRenderState, HumanoidArm arm, PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light, CallbackInfo info) {
        if (entityState instanceof AvatarRenderState playerState) {
            PlayerEntityRenderStateVarsInterface betterState = ((PlayerEntityRenderStateVarsInterface) (Object) playerState);
            InteractionHand hand = betterState.get_cryingShieldHand();
            if (hand != null && CryingClient.compareHandtoArm(hand, arm, playerState.mainArm)) {
                matrices.pushPose();
                float tickDelta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
                float age = betterState.get_realAge() + tickDelta;

                float offset = (float) Math.sin(age / 8.0f) * 0.1f;
                matrices.translate(0.0F, 0.2F + offset, 0.0F);

                float spin = (age * 4f) % 360f;
                matrices.mulPose(Axis.YP.rotationDegrees(spin));

                matrices.mulPose(Axis.XP.rotationDegrees(-105.0F));
                matrices.mulPose(Axis.YP.rotationDegrees(180.0F));
                boolean bl = arm == HumanoidArm.LEFT;
                matrices.translate((float)(bl ? -1 : 1) / 16.0F, 0.125F, -0.625F);
                if (bl)
                    matrices.translate(-0.35d, 0d, 0d);
                else
                    matrices.translate(0.35d, 0d, 0d);
                    
                itemRenderState.submit(matrices, orderedRenderCommandQueue, light, OverlayTexture.NO_OVERLAY, entityState.outlineColor);
                matrices.popPose();
                info.cancel();
            }
        }
    }
}
