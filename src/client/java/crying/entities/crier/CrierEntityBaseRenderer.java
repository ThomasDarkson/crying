package crying.entities.crier;

import crying.Crying;
import crying.entities.CrierEntity;
import crying.feature.SpinningCryingShieldFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public abstract class CrierEntityBaseRenderer<T extends CrierEntity, S extends CrierEntityRenderState, M extends CrierEntityModel<S>> extends BipedEntityRenderer<T, S, M> {
    private static final Identifier TEXTURE = Identifier.of(Crying.ID, "textures/entity/crier/crier.png");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected CrierEntityBaseRenderer(EntityRendererFactory.Context context, M mainModel, M babyMainModel, EquipmentModelData<M> equipmentModelData, EquipmentModelData<M> equipmentModelData2) {
        super(context, mainModel, babyMainModel, 0.5F);
        this.addFeature(new ArmorFeatureRenderer(this, equipmentModelData, equipmentModelData2, context.getEquipmentRenderer()));
        this.addFeature((FeatureRenderer<S, M>) new SpinningCryingShieldFeatureRenderer((FeatureRendererContext<CrierEntityRenderState, CrierEntityModel<CrierEntityRenderState>>) this));
    }

    @Override
    public Identifier getTexture(S crierEntityRenderState) {
        return TEXTURE;
    }

    @Override
    public void updateRenderState(T crierEntity, S state, float tickDelta) {
        super.updateRenderState(crierEntity, state, tickDelta);
        state.forlorn = crierEntity.getSecondPhase();
        state.shieldHealth = crierEntity.getShieldHealth();
        state.realAge = crierEntity.age;

        float time = crierEntity.age + tickDelta;
        state.headSpinDegrees = time * (state.shieldHealth <= 0F ? (state.forlorn ? 2.5F : 5.0F) : 10.0F);
        state.headFloatOffset = (float) Math.sin(time * 0.15F) * 0.3F; 
    }
}
