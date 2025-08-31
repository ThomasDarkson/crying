package crying.renderers;

import crying.Crying;
import crying.entities.GrapplingHookEntity;
import crying.states.GrapplingHookEntityRenderState;

import org.joml.Matrix4f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState.LeashData;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class GrapplingHookRenderer extends EntityRenderer<GrapplingHookEntity, GrapplingHookEntityRenderState> {
    public GrapplingHookRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    public void updateRenderState(GrapplingHookEntity entity, GrapplingHookEntityRenderState state, float tickProgress) {
        super.updateRenderState(entity, state, tickProgress);
        if (state.leashDatas != null) {
            LeashData data = state.leashDatas.getFirst();
            if (data != null) {
                state.hookOffset = data.offset;
                state.startHookPos = data.startPos;
                state.endHookPos = data.endPos;
                state.leashDatas = null;
            }   
        }
    }

    public void render(GrapplingHookEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        itemRenderer.renderItem(new ItemStack(Crying.CRYING_GRAPPLING_HOOK_TIP), ItemDisplayContext.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, null, 0);
        matrices.pop();

        renderHook(matrices, vertexConsumers, state, light);
    }

    @Override
    public GrapplingHookEntityRenderState createRenderState() {
        return new GrapplingHookEntityRenderState();
    }

    private static void renderHook(MatrixStack matrices, VertexConsumerProvider vertexConsumers, GrapplingHookEntityRenderState state, int light) {
        float g = (float)(state.endHookPos.x - state.startHookPos.x);
        float h = (float)(state.endHookPos.y - state.startHookPos.y);
        float i = (float)(state.endHookPos.z - state.startHookPos.z);
        float j = MathHelper.inverseSqrt(g * g + i * i) * 0.025F / 2.0F;
        float k = i * j;
        float l = g * j;
        matrices.push();
        matrices.translate(state.hookOffset);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLeash());
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        int m;
        for(m = 0; m <= 24; ++m) {
            renderHookSegment(vertexConsumer, matrix4f, g, h, i, 0.025F, 0.025F, k, l, m, false);
        }

        for(m = 24; m >= 0; --m) {
            renderHookSegment(vertexConsumer, matrix4f, g, h, i, 0.025F, 0.0F, k, l, m, true);
        }

        matrices.pop();
    }

    private static void renderHookSegment(VertexConsumer vertexConsumer, Matrix4f matrix, float leashedEntityX, float leashedEntityY, float leashedEntityZ, float f, float g, float h, float i, int segmentIndex, boolean isLeashKnot) {
        float j = (float)segmentIndex / 24.0F;

        float[] color1 = {0.105f, 0.059f, 0.188f};
        float[] color2 = {0.059f, 0.043f, 0.086f};
        float[] chosenColor = (segmentIndex % 2 == (isLeashKnot ? 1 : 0)) ? color1 : color2;

        float r = chosenColor[0];
        float gColor = chosenColor[1];
        float b = chosenColor[2];

        float x = leashedEntityX * j;
        float y = leashedEntityY > 0.0F ? leashedEntityY * j * j : leashedEntityY - leashedEntityY * (1.0F - j) * (1.0F - j);
        float z = leashedEntityZ * j;

        vertexConsumer.vertex(matrix, x - h, y + g, z + i).color(r, gColor, b, 1.0F).light(15728880);
        vertexConsumer.vertex(matrix, x + h, y + f - g, z - i).color(r, gColor, b, 1.0F).light(15728880);
    }
}