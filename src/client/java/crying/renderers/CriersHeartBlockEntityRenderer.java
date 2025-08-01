package crying.renderers;

import crying.entities.CriersHeartBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CriersHeartBlockEntityRenderer implements BlockEntityRenderer<CriersHeartBlockEntity> {
    private static final float PI = (float) Math.PI;

    public CriersHeartBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(CriersHeartBlockEntity entity, float tickProgress, MatrixStack matrices,
        VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        matrices.push();

        float phase = entity.heartbeatPhase + tickProgress * ((entity.SPEED + entity.speedModifier) / 100);
        phase %= 1.0F;

        float scale = getScale(phase);
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.scale(scale, scale, scale);
        matrices.translate(-0.5, -0.5, -0.5);

        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getCachedState(), matrices, vertexConsumers, light, overlay);
        matrices.pop();
    }

    private static float getScale(float phase) {
        if (phase < 0.3f) 
            return 1.0f + 0.15f * MathHelper.sin(phase / 0.3f * PI);
        else if (phase < 0.6f)
            return 1.0f + 0.075f * MathHelper.sin((phase - 0.3f) / 0.3f * PI);
        else 
            return 1.0f;
    }
}
