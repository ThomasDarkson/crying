package crying.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import crying.blocks.food.CryingFoodBlock;
import crying.entities.CryingFoodEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CryingFoodEntityRenderer implements BlockEntityRenderer<CryingFoodEntity> {
    public CryingFoodEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }
    
    @Override
    public void render(CryingFoodEntity entity, float tickProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, Vec3 cameraPos) {
        BlockState state = entity.getBlockState();
        if (state != null) {
            Block block = state.getBlock();
            if (block != null && block instanceof CryingFoodBlock foodBlock && foodBlock.stack() != null) {
                matrices.pushPose();
                matrices.translate(0.5F, 0.01F, 0.5F);
                matrices.mulPose(Axis.XP.rotationDegrees(90F));
                matrices.scale(0.5f, 0.5f, 0.5f);
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                itemRenderer.renderStatic(foodBlock.stack(), ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, null, 0);
                matrices.popPose();
            }
        }
    }

    @Override
    public int getViewDistance() {
        return 103;
    }
}