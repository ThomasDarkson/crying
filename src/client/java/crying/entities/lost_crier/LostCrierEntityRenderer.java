package crying.entities.lost_crier;

import crying.entities.LostCrierEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class LostCrierEntityRenderer extends LostCrierEntityBaseRenderer<LostCrierEntity, LostCrierEntityRenderState, LostCrierEntityModel<LostCrierEntityRenderState>> {
    public LostCrierEntityRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_BABY, 0.75F);
    }

    public LostCrierEntityRenderState createRenderState() {
        return new LostCrierEntityRenderState();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public LostCrierEntityRenderer(EntityRendererProvider.Context ctx, ModelLayerLocation model, ModelLayerLocation babyModel, float scale) {
        super(ctx, new LostCrierEntityModel(ctx.bakeLayer(model)), new LostCrierEntityModel(ctx.bakeLayer(babyModel)), scale);
    }

    @Override
    public Identifier getTextureLocation(LostCrierEntityRenderState renderState) {
        return super.getTextureLocation(renderState);
    }

    @Override
    protected boolean isShaking(LostCrierEntityRenderState crierEntityRenderState) {
        return false;
    }
}
