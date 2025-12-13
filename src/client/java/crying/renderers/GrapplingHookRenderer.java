package crying.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import crying.Crying;
import crying.entities.GrapplingHookEntity;
import crying.states.GrapplingHookEntityRenderState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class GrapplingHookRenderer extends EntityRenderer<GrapplingHookEntity, GrapplingHookEntityRenderState> {
    ItemModelResolver itemManager;
    public GrapplingHookRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        itemManager = ctx.getItemModelResolver();
    }

    @Override
    public void extractRenderState(GrapplingHookEntity entity, GrapplingHookEntityRenderState state, float tickProgress) {
        super.extractRenderState(entity, state, tickProgress);
    }

    @Override    
    public void submit(GrapplingHookEntityRenderState renderState, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        matrices.pushPose();
        ItemStackRenderState state = new ItemStackRenderState();
        itemManager.updateForTopItem(state, new ItemStack(Crying.CRYING_GRAPPLING_HOOK_TIP), ItemDisplayContext.NONE, null, null, 0);
        state.submit(matrices, queue, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        matrices.popPose();
        
        super.submit(renderState, matrices, queue, cameraState);
    }

    @Override
    public GrapplingHookEntityRenderState createRenderState() {
        return new GrapplingHookEntityRenderState();
    }
}