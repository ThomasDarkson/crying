package crying.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import crying.Crying;
import crying.entities.crier.CrierEntityModel;
import crying.entities.crier.CrierEntityRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SpinningCryingShieldFeatureRenderer extends RenderLayer<CrierEntityRenderState, CrierEntityModel<CrierEntityRenderState>> {
    public SpinningCryingShieldFeatureRenderer(RenderLayerParent<CrierEntityRenderState, CrierEntityModel<CrierEntityRenderState>> context) {
        super(context);
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, CrierEntityRenderState state, float limbAngle, float limbDistance) {
        if (state.shieldHealth > 0) {
            matrices.pushPose();
            
            float tickDelta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
            float age = state.realAge + tickDelta;

            float offset = (float) Math.sin(age / 8.0f) * 0.1f;
            matrices.translate(0.0F, 0.2F + offset, 0.0F);

            float spin = (age * 4f) % 360f;
            matrices.mulPose(Axis.YP.rotationDegrees(spin));

            matrices.mulPose(Axis.XP.rotationDegrees(-105.0F));
            matrices.mulPose(Axis.YP.rotationDegrees(180.0F));
            matrices.translate(1F / 16.0F, 0.125F, -0.625F);
            matrices.translate(-0.35d, 0d, 0d);
                
            ItemModelResolver m = new ItemModelResolver(Minecraft.getInstance().getModelManager());
            ItemStackRenderState state2 = new ItemStackRenderState();

            m.updateForTopItem(state2, new ItemStack(Crying.CRYING_SHIELD), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, null, null, 0);
            state2.render(matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY);

            matrices.popPose();
        }
    }
}
