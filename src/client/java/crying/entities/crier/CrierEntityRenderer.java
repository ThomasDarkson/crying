package crying.entities.crier;

import crying.Crying;
import crying.entities.CrierEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class CrierEntityRenderer extends CrierEntityBaseRenderer<CrierEntity, CrierEntityRenderState, CrierEntityModel<CrierEntityRenderState>> {
    private static final Identifier FORLORN_TEXTURE = Identifier.fromNamespaceAndPath(Crying.ID, "textures/entity/crier/forlorn_crier.png");

    public CrierEntityRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_BABY, ModelLayers.ZOMBIE_ARMOR, ModelLayers.ZOMBIE_BABY_ARMOR);
    }

    public CrierEntityRenderState createRenderState() {
        return new CrierEntityRenderState();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public CrierEntityRenderer(EntityRendererProvider.Context ctx, ModelLayerLocation layer, ModelLayerLocation legsArmorLayer, ArmorModelSet<ModelLayerLocation> equipmentModelData, ArmorModelSet<ModelLayerLocation> equipmentModelData2) {
        super(ctx, new CrierEntityModel(ctx.bakeLayer(layer)), new CrierEntityModel(ctx.bakeLayer(legsArmorLayer)), ArmorModelSet.bake(equipmentModelData, ctx.getModelSet(), CrierEntityModel::new), ArmorModelSet.bake(equipmentModelData2, ctx.getModelSet(), CrierEntityModel::new));
    }

    @Override
    public Identifier getTextureLocation(CrierEntityRenderState renderState) {
        if (renderState.forlorn)
            return FORLORN_TEXTURE;

        return super.getTextureLocation(renderState);
    }

    @Override
    protected boolean isShaking(CrierEntityRenderState crierEntityRenderState) {
        return crierEntityRenderState.forlorn;
    }
}
