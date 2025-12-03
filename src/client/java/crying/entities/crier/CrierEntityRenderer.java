package crying.entities.crier;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import crying.Crying;
import crying.entities.CrierEntity;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EquipmentModelData;

public class CrierEntityRenderer extends CrierEntityBaseRenderer<CrierEntity, CrierEntityRenderState, CrierEntityModel<CrierEntityRenderState>> {
    private static final Identifier FORLORN_TEXTURE = Identifier.of(Crying.ID, "textures/entity/crier/forlorn_crier.png");

    public CrierEntityRenderer(EntityRendererFactory.Context context) {
        this(context, EntityModelLayers.ZOMBIE, EntityModelLayers.ZOMBIE_BABY, EntityModelLayers.ZOMBIE_EQUIPMENT, EntityModelLayers.ZOMBIE_BABY_EQUIPMENT);
    }

    public CrierEntityRenderState createRenderState() {
        return new CrierEntityRenderState();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public CrierEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legsArmorLayer, EquipmentModelData<EntityModelLayer> equipmentModelData, EquipmentModelData<EntityModelLayer> equipmentModelData2) {
        super(ctx, new CrierEntityModel(ctx.getPart(layer)), new CrierEntityModel(ctx.getPart(legsArmorLayer)), EquipmentModelData.mapToEntityModel(equipmentModelData, ctx.getEntityModels(), CrierEntityModel::new), EquipmentModelData.mapToEntityModel(equipmentModelData2, ctx.getEntityModels(), CrierEntityModel::new));
    }

    @Override
    public Identifier getTexture(CrierEntityRenderState renderState) {
        if (renderState.forlorn)
            return FORLORN_TEXTURE;

        return super.getTexture(renderState);
    }

    @Override
    protected boolean isShaking(CrierEntityRenderState crierEntityRenderState) {
        return crierEntityRenderState.forlorn;
    }
}
