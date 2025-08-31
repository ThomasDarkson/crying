package crying.entities.lost_crier;

import crying.Crying;
import crying.entities.LostCrierEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public abstract class LostCrierEntityBaseRenderer<T extends LostCrierEntity, S extends LostCrierEntityRenderState, M extends LostCrierEntityModel<S>> extends BipedEntityRenderer<T, S, M> {
    private static final Identifier TEXTURE = Identifier.of(Crying.ID, "textures/entity/lost_crier/lost_crier.png");

    @SuppressWarnings({ "rawtypes" })
    public LostCrierEntityBaseRenderer(EntityRendererFactory.Context context, M model, M babyModel, float scale) {
        super(context, model, babyModel, 0.75F);
    }

    @Override
    public Identifier getTexture(S crierEntityRenderState) {
        return TEXTURE;
    }

    @Override
    public void updateRenderState(T crierEntity, S crierEntityRenderState, float f) {
        super.updateRenderState(crierEntity, crierEntityRenderState, f);
    }
}
