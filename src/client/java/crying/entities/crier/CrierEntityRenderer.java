package crying.entities.crier;

import crying.Crying;
import crying.entities.CrierEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CrierEntityRenderer extends CrierEntityBaseRenderer<CrierEntity, CrierEntityRenderState, CrierEntityModel<CrierEntityRenderState>> {
    private static final ResourceLocation FORLORN_TEXTURE = ResourceLocation.fromNamespaceAndPath(Crying.ID, "textures/entity/crier/forlorn_crier.png");

    public CrierEntityRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_BABY, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR, ModelLayers.ZOMBIE_BABY_INNER_ARMOR, ModelLayers.ZOMBIE_BABY_OUTER_ARMOR);
    }

    public CrierEntityRenderState createRenderState() {
        return new CrierEntityRenderState();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public CrierEntityRenderer(EntityRendererProvider.Context ctx, ModelLayerLocation layer, ModelLayerLocation legsArmorLayer, ModelLayerLocation bodyArmorLayer, ModelLayerLocation entityModelLayer, ModelLayerLocation entityModelLayer2, ModelLayerLocation entityModelLayer3) {
        super(ctx, new CrierEntityModel(ctx.bakeLayer(layer)), new CrierEntityModel(ctx.bakeLayer(legsArmorLayer)), new CrierEntityModel(ctx.bakeLayer(bodyArmorLayer)), new CrierEntityModel(ctx.bakeLayer(entityModelLayer)), new CrierEntityModel(ctx.bakeLayer(entityModelLayer2)), new CrierEntityModel(ctx.bakeLayer(entityModelLayer3)));
    }

    @Override
    public ResourceLocation getTextureLocation(CrierEntityRenderState renderState) {
        if (renderState.forlorn)
            return FORLORN_TEXTURE;

        return super.getTextureLocation(renderState);
    }

    @Override
    protected boolean isShaking(CrierEntityRenderState crierEntityRenderState) {
        return crierEntityRenderState.forlorn;
    }
}
