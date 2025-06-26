package crying.tools.entities;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import crying.tools.Crying;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;

public class CrierEntityRenderer extends CrierEntityBaseRenderer<CrierEntity, CrierEntityRenderState, CrierEntityModel<CrierEntityRenderState>> {
    private static final Identifier FORLORN_TEXTURE = Identifier.of(Crying.ID, "textures/entity/crier/forlorn_crier.png");

    public CrierEntityRenderer(EntityRendererFactory.Context context) {
        this(context, EntityModelLayers.ZOMBIE, EntityModelLayers.ZOMBIE_BABY, EntityModelLayers.ZOMBIE_INNER_ARMOR, EntityModelLayers.ZOMBIE_OUTER_ARMOR, EntityModelLayers.ZOMBIE_BABY_INNER_ARMOR, EntityModelLayers.ZOMBIE_BABY_OUTER_ARMOR);
    }

    public CrierEntityRenderState createRenderState() {
        return new CrierEntityRenderState();
    }

    @SuppressWarnings("unchecked")
    public CrierEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legsArmorLayer, EntityModelLayer bodyArmorLayer, EntityModelLayer entityModelLayer, EntityModelLayer entityModelLayer2, EntityModelLayer entityModelLayer3) {
        super(ctx, new CrierEntityModel(ctx.getPart(layer)), new CrierEntityModel(ctx.getPart(legsArmorLayer)), new CrierEntityModel(ctx.getPart(bodyArmorLayer)), new CrierEntityModel(ctx.getPart(entityModelLayer)), new CrierEntityModel(ctx.getPart(entityModelLayer2)), new CrierEntityModel(ctx.getPart(entityModelLayer3)));
    }

    @Override
    public Identifier getTexture(CrierEntityRenderState renderState) {
        if (renderState.forlorn)
            return FORLORN_TEXTURE;

        return super.getTexture(renderState);
    }

    @Override
    protected boolean isShaking(CrierEntityRenderState crierEntityRenderState) {
        return false;
    }
}
