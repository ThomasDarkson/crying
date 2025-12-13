package crying.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import crying.blocks.food.CryingFoodBlock;
import crying.entities.CryingFoodEntity;
import crying.states.CryingFoodEntityRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

public class CryingFoodEntityRenderer implements BlockEntityRenderer<CryingFoodEntity, CryingFoodEntityRenderState> {
    ItemModelResolver itemManager;
    public CryingFoodEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        itemManager = ctx.itemModelResolver();
    }
    
    @Override
    public void submit(CryingFoodEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        if (state != null && state.stack != null) {
            matrices.pushPose();
            matrices.translate(0.5F, 0.01F, 0.5F);
            matrices.mulPose(Axis.XP.rotationDegrees(90F));
            matrices.scale(0.5f, 0.5f, 0.5f);
            ItemStackRenderState s = new ItemStackRenderState();
            itemManager.updateForTopItem(s, state.stack, ItemDisplayContext.NONE, null, null, 0);
            s.submit(matrices, queue, 15728880, OverlayTexture.NO_OVERLAY, 0);
            matrices.popPose();
        }
    }

    @Override
    public void extractRenderState(CryingFoodEntity blockEntity, CryingFoodEntityRenderState state, float tickProgress, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay);

        if (blockEntity.getBlockState().getBlock() instanceof CryingFoodBlock block) {
            state.stack = block.stack();
        } 
    }

    @Override
    public int getViewDistance() {
        return 103;
    }

    @Override
    public CryingFoodEntityRenderState createRenderState() {
        return new CryingFoodEntityRenderState();
    }
}