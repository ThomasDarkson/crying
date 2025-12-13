package crying.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import crying.Crying;
import crying.entities.GrapplingHookEntity;
import crying.states.GrapplingHookEntityRenderState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class GrapplingHookRenderer extends EntityRenderer<GrapplingHookEntity, GrapplingHookEntityRenderState> {
    public GrapplingHookRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    public void updateRenderState(GrapplingHookEntity entity, GrapplingHookEntityRenderState state, float tickProgress) {
        super.extractRenderState(entity, state, tickProgress);

        state.leashState.startBlockLight = 15;
        state.leashState.startSkyLight = 15;
        state.leashState.endBlockLight = 15;
        state.leashState.endSkyLight = 15;
    }

    public void render(GrapplingHookEntityRenderState state, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
        matrices.pushPose();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderStatic(new ItemStack(Crying.CRYING_GRAPPLING_HOOK_TIP), ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, null, 0);
        matrices.popPose();

        super.render(state, matrices, vertexConsumers, light);
    }

    @Override
    public GrapplingHookEntityRenderState createRenderState() {
        return new GrapplingHookEntityRenderState();
    }
}