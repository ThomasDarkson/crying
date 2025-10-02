package crying.renderers;

import crying.Crying;
import crying.entities.GrapplingHookEntity;
import crying.states.GrapplingHookEntityRenderState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class GrapplingHookRenderer extends EntityRenderer<GrapplingHookEntity, GrapplingHookEntityRenderState> {
    ItemModelManager itemManager;
    public GrapplingHookRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        itemManager = ctx.getItemModelManager();
    }

    @Override
    public void updateRenderState(GrapplingHookEntity entity, GrapplingHookEntityRenderState state, float tickProgress) {
        super.updateRenderState(entity, state, tickProgress);
    }

    @Override    
    public void render(GrapplingHookEntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();
        ItemRenderState state = new ItemRenderState();
        itemManager.clearAndUpdate(state, new ItemStack(Crying.CRYING_GRAPPLING_HOOK_TIP), ItemDisplayContext.NONE, null, null, 0);
        state.render(matrices, queue, renderState.light, OverlayTexture.DEFAULT_UV, 0);
        matrices.pop();
        
        super.render(renderState, matrices, queue, cameraState);
    }

    @Override
    public GrapplingHookEntityRenderState createRenderState() {
        return new GrapplingHookEntityRenderState();
    }
}