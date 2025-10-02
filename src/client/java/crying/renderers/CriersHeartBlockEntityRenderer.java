package crying.renderers;

import crying.entities.CriersHeartBlockEntity;
import crying.states.CriersHeartBlockEntityRenderState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CriersHeartBlockEntityRenderer implements BlockEntityRenderer<CriersHeartBlockEntity, CriersHeartBlockEntityRenderState> {
    private static final float PI = (float) Math.PI;

    public CriersHeartBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(CriersHeartBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (state.state != null) {
            float phase = state.heartbeatPhase + state.tickProgress * ((state.SPEED + state.speedModifier) / 100);
            phase %= 1.0F;

            float scale = getScale(phase);
            matrices.push();
            matrices.translate(0.5, 0.5, 0.5);
            matrices.scale(scale, scale, scale);
            matrices.translate(-0.5, -0.5, -0.5);

            queue.submitBlock(matrices, state.state, 15728880, OverlayTexture.DEFAULT_UV, 0);
            matrices.pop();
        }
    }

    private static float getScale(float phase) {
        if (phase < 0.3f) 
            return 1.0f + 0.15f * MathHelper.sin(phase / 0.3f * PI);
        else if (phase < 0.6f)
            return 1.0f + 0.075f * MathHelper.sin((phase - 0.3f) / 0.3f * PI);
        else 
            return 1.0f;
    }

    @Override
    public void updateRenderState(CriersHeartBlockEntity blockEntity, CriersHeartBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderState.updateBlockEntityRenderState(blockEntity, state, crumblingOverlay);
        
        state.SPEED = blockEntity.SPEED;
        state.heartbeatPhase = blockEntity.heartbeatPhase;
        state.speedModifier = blockEntity.speedModifier;
        state.tickProgress = tickProgress;
        state.state = blockEntity.getCachedState();
    }

    @Override
    public CriersHeartBlockEntityRenderState createRenderState() {
        return new CriersHeartBlockEntityRenderState();
    }
}
