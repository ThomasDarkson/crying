package crying.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import crying.entities.CriersHeartBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class CriersHeartBlockEntityRenderer implements BlockEntityRenderer<CriersHeartBlockEntity> {
    private static final float PI = (float) Math.PI;

    public CriersHeartBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(CriersHeartBlockEntity entity, float tickProgress, PoseStack matrices,
        MultiBufferSource vertexConsumers, int light, int overlay, Vec3 cameraPos) {
        matrices.pushPose();

        float phase = entity.heartbeatPhase + tickProgress * ((entity.SPEED + entity.speedModifier) / 100);
        phase %= 1.0F;

        float scale = getScale(phase);
        matrices.pushPose();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.scale(scale, scale, scale);
        matrices.translate(-0.5, -0.5, -0.5);

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(entity.getBlockState(), matrices, vertexConsumers, light, overlay);
        matrices.popPose();
    }

    private static float getScale(float phase) {
        if (phase < 0.3f) 
            return 1.0f + 0.15f * Mth.sin(phase / 0.3f * PI);
        else if (phase < 0.6f)
            return 1.0f + 0.075f * Mth.sin((phase - 0.3f) / 0.3f * PI);
        else 
            return 1.0f;
    }
}
