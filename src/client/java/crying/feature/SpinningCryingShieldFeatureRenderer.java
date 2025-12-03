package crying.feature;

import crying.Crying;
import crying.entities.crier.CrierEntityModel;
import crying.entities.crier.CrierEntityRenderState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class SpinningCryingShieldFeatureRenderer extends FeatureRenderer<CrierEntityRenderState, CrierEntityModel<CrierEntityRenderState>> {
    public SpinningCryingShieldFeatureRenderer(FeatureRendererContext<CrierEntityRenderState, CrierEntityModel<CrierEntityRenderState>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, CrierEntityRenderState state, float limbAngle, float limbDistance) {
        if (state.shieldHealth > 0) {
            matrices.push();
            
            float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false);
            float age = state.realAge + tickDelta;

            float offset = (float) Math.sin(age / 8.0f) * 0.1f;
            matrices.translate(0.0F, 0.2F + offset, 0.0F);

            float spin = (age * 4f) % 360f;
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(spin));

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-105.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
            matrices.translate(1F / 16.0F, 0.125F, -0.625F);
            matrices.translate(-0.35d, 0d, 0d);
                
            ItemModelManager m = new ItemModelManager(MinecraftClient.getInstance().getBakedModelManager());
            ItemRenderState state2 = new ItemRenderState();

            m.clearAndUpdate(state2, new ItemStack(Crying.CRYING_SHIELD), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, null, null, 0);
            state2.render(matrices, queue, light, OverlayTexture.DEFAULT_UV, 0);

            matrices.pop();
        }
    }
}
