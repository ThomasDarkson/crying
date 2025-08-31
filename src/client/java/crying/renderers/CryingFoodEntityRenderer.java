package crying.renderers;

import crying.blocks.food.CryingFoodBlock;
import crying.entities.CryingFoodEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class CryingFoodEntityRenderer implements BlockEntityRenderer<CryingFoodEntity> {
    public CryingFoodEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }
    
    @Override
    public void render(CryingFoodEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        BlockState state = entity.getCachedState();
        if (state != null) {
            Block block = state.getBlock();
            if (block != null && block instanceof CryingFoodBlock foodBlock && foodBlock.stack() != null) {
                matrices.push();
                matrices.translate(0.5F, 0.01F, 0.5F);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90F));
                matrices.scale(0.5f, 0.5f, 0.5f);
                ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
                itemRenderer.renderItem(foodBlock.stack(), ItemDisplayContext.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, null, 0);
                matrices.pop();
            }
        }
    }

    @Override
    public int getRenderDistance() {
        return 103;
    }
}