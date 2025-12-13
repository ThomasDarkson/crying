package crying.entities.lost_crier;

import crying.Crying;
import crying.entities.LostCrierEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public abstract class LostCrierEntityBaseRenderer<T extends LostCrierEntity, S extends LostCrierEntityRenderState, M extends LostCrierEntityModel<S>> extends HumanoidMobRenderer<T, S, M> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Crying.ID, "textures/entity/lost_crier/lost_crier.png");

    public LostCrierEntityBaseRenderer(EntityRendererProvider.Context context, M model, M babyModel, float scale) {
        super(context, model, babyModel, 0.75F);
    }

    @Override
    public Identifier getTextureLocation(S crierEntityRenderState) {
        return TEXTURE;
    }

    @Override
    public void extractRenderState(T crierEntity, S crierEntityRenderState, float f) {
        super.extractRenderState(crierEntity, crierEntityRenderState, f);
    }
}
