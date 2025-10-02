package crying.entities.crier;

import crying.Crying;
import crying.entities.CrierEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public abstract class CrierEntityBaseRenderer<T extends CrierEntity, S extends CrierEntityRenderState, M extends CrierEntityModel<S>> extends BipedEntityRenderer<T, S, M> {
    private static final Identifier TEXTURE = Identifier.of(Crying.ID, "textures/entity/crier/crier.png");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected CrierEntityBaseRenderer(EntityRendererFactory.Context context, M mainModel, M babyMainModel, EquipmentModelData<M> equipmentModelData, EquipmentModelData<M> equipmentModelData2) {
        super(context, mainModel, babyMainModel, 0.5F);
        this.addFeature(new ArmorFeatureRenderer(this, equipmentModelData, equipmentModelData2, context.getEquipmentRenderer()));
    }

    @Override
    public Identifier getTexture(S crierEntityRenderState) {
        return TEXTURE;
    }

    @Override
    public void updateRenderState(T crierEntity, S crierEntityRenderState, float f) {
        super.updateRenderState(crierEntity, crierEntityRenderState, f);
        crierEntityRenderState.forlorn = crierEntity.getSecondPhase();
    }
}
