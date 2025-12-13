package crying.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import crying.entities.CriersHeartBlockEntity;
import crying.states.CriersHeartBlockEntityRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class CriersHeartBlockEntityRenderer implements BlockEntityRenderer<CriersHeartBlockEntity, CriersHeartBlockEntityRenderState> {
    private static final float PI = (float) Math.PI;

    public CriersHeartBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void submit(CriersHeartBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        if (state.state != null) {
            float phase = state.heartbeatPhase + state.tickProgress * ((state.SPEED + state.speedModifier) / 100);
            phase %= 1.0F;

            float scale = getScale(phase);
            matrices.pushPose();
            matrices.translate(0.5, 0.5, 0.5);
            matrices.scale(scale, scale, scale);
            matrices.translate(-0.5, -0.5, -0.5);

            queue.submitBlock(matrices, state.state, 15728880, OverlayTexture.NO_OVERLAY, 0);
            matrices.popPose();
        }
    }

    private static float getScale(float phase) {
        if (phase < 0.3f) 
            return 1.0f + 0.15f * Mth.sin(phase / 0.3f * PI);
        else if (phase < 0.6f)
            return 1.0f + 0.075f * Mth.sin((phase - 0.3f) / 0.3f * PI);
        else 
            return 1.0f;
    }

    @Override
    public void extractRenderState(CriersHeartBlockEntity blockEntity, CriersHeartBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay);
        
        state.SPEED = blockEntity.SPEED;
        state.heartbeatPhase = blockEntity.heartbeatPhase;
        state.speedModifier = blockEntity.speedModifier;
        state.tickProgress = tickProgress;
        state.state = blockEntity.getBlockState();
    }

    @Override
    public CriersHeartBlockEntityRenderState createRenderState() {
        return new CriersHeartBlockEntityRenderState();
    }
}
