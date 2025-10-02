package crying.renderers;

import crying.Crying;
import crying.entities.GrapplingHookEntity;
import crying.states.GrapplingHookEntityRenderState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class GrapplingHookRenderer extends EntityRenderer<GrapplingHookEntity, GrapplingHookEntityRenderState> {
    public GrapplingHookRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    public void updateRenderState(GrapplingHookEntity entity, GrapplingHookEntityRenderState state, float tickProgress) {
        super.updateRenderState(entity, state, tickProgress);
    }

    public void render(GrapplingHookEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        itemRenderer.renderItem(new ItemStack(Crying.CRYING_GRAPPLING_HOOK_TIP), ItemDisplayContext.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, null, 0);
        matrices.pop();

        super.render(state, matrices, vertexConsumers, light);
    }

    @Override
    public GrapplingHookEntityRenderState createRenderState() {
        return new GrapplingHookEntityRenderState();
    }
}