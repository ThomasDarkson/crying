package crying.renderers;

import crying.blocks.food.CryingFoodBlock;
import crying.entities.CryingFoodEntity;
import crying.states.CryingFoodEntityRenderState;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class CryingFoodEntityRenderer implements BlockEntityRenderer<CryingFoodEntity, CryingFoodEntityRenderState> {
    ItemModelManager itemManager;
    public CryingFoodEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        itemManager = ctx.itemModelManager();
    }
    
    @Override
    public void render(CryingFoodEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (state != null && state.stack != null) {
            matrices.push();
            matrices.translate(0.5F, 0.01F, 0.5F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90F));
            matrices.scale(0.5f, 0.5f, 0.5f);
            ItemRenderState s = new ItemRenderState();
            itemManager.clearAndUpdate(s, state.stack, ItemDisplayContext.NONE, null, null, 0);
            s.render(matrices, queue, 15728880, OverlayTexture.DEFAULT_UV, 0);
            matrices.pop();
        }
    }

    @Override
    public void updateRenderState(CryingFoodEntity blockEntity, CryingFoodEntityRenderState state, float tickProgress, Vec3d cameraPos, ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderState.updateBlockEntityRenderState(blockEntity, state, crumblingOverlay);

        if (blockEntity.getCachedState().getBlock() instanceof CryingFoodBlock block) {
            state.stack = block.stack();
        } 
    }

    @Override
    public int getRenderDistance() {
        return 103;
    }

    @Override
    public CryingFoodEntityRenderState createRenderState() {
        return new CryingFoodEntityRenderState();
    }
}