package crying.entities.crier;

import crying.Crying;
import crying.entities.CrierEntity;
import crying.feature.SpinningCryingShieldFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public abstract class CrierEntityBaseRenderer<T extends CrierEntity, S extends CrierEntityRenderState, M extends CrierEntityModel<S>> extends HumanoidMobRenderer<T, S, M> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Crying.ID, "textures/entity/crier/crier.png");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected CrierEntityBaseRenderer(EntityRendererProvider.Context context, M mainModel, M babyMainModel, ArmorModelSet<M> equipmentModelData, ArmorModelSet<M> equipmentModelData2) {
        super(context, mainModel, babyMainModel, 0.5F);
        this.addLayer(new HumanoidArmorLayer(this, equipmentModelData, equipmentModelData2, context.getEquipmentRenderer()));
        this.addLayer((RenderLayer<S, M>) new SpinningCryingShieldFeatureRenderer((RenderLayerParent<CrierEntityRenderState, CrierEntityModel<CrierEntityRenderState>>) this));
    }

    @Override
    public ResourceLocation getTextureLocation(S crierEntityRenderState) {
        return TEXTURE;
    }

    @Override
    public void extractRenderState(T crierEntity, S state, float tickDelta) {
        super.extractRenderState(crierEntity, state, tickDelta);
        state.forlorn = crierEntity.getSecondPhase();
        state.shieldHealth = crierEntity.getShieldHealth();
        state.realAge = crierEntity.tickCount;

        float time = crierEntity.tickCount + tickDelta;
        state.headSpinDegrees = time * (state.shieldHealth <= 0F ? (state.forlorn ? 2.5F : 5.0F) : 10.0F);
        state.headFloatOffset = (float) Math.sin(time * 0.15F) * 0.3F; 
    }
}
