package crying.entities.lost_crier;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import crying.entities.LostCrierEntity;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;

public class LostCrierEntityRenderer extends LostCrierEntityBaseRenderer<LostCrierEntity, LostCrierEntityRenderState, LostCrierEntityModel<LostCrierEntityRenderState>> {
    public LostCrierEntityRenderer(EntityRendererFactory.Context context) {
        this(context, EntityModelLayers.ZOMBIE, EntityModelLayers.ZOMBIE_BABY, 0.75F);
    }

    public LostCrierEntityRenderState createRenderState() {
        return new LostCrierEntityRenderState();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public LostCrierEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer model, EntityModelLayer babyModel, float scale) {
        super(ctx, new LostCrierEntityModel(ctx.getPart(model)), new LostCrierEntityModel(ctx.getPart(babyModel)), scale);
    }

    @Override
    public Identifier getTexture(LostCrierEntityRenderState renderState) {
        return super.getTexture(renderState);
    }

    @Override
    protected boolean isShaking(LostCrierEntityRenderState crierEntityRenderState) {
        return false;
    }
}
