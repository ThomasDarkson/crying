package crying.tools.entities;

import crying.tools.Crying;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public abstract class CrierEntityBaseRenderer<T extends CrierEntity, S extends CrierEntityRenderState, M extends CrierEntityModel<S>> extends BipedEntityRenderer<T, S, M> {
   private static final Identifier TEXTURE = Identifier.of(Crying.ID, "textures/entity/crier/crier.png");

   @SuppressWarnings("unchecked")
   protected CrierEntityBaseRenderer(EntityRendererFactory.Context context, M mainModel, M babyMainModel, M armorInnerModel, M armorOuterModel, M babyArmorInnerModel, M babyArmorOuterModel) {
      super(context, mainModel, babyMainModel, 0.75F);
      this.addFeature(new ArmorFeatureRenderer(this, armorInnerModel, armorOuterModel, babyArmorInnerModel, babyArmorOuterModel, context.getEquipmentRenderer()));
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
