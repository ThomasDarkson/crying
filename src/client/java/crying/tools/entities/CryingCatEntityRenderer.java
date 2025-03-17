package crying.tools.entities;

import crying.tools.Crying;
import net.minecraft.client.render.entity.CatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.Identifier;

public class CryingCatEntityRenderer extends CatEntityRenderer {
    private static final Identifier TEXTURE = Identifier.of(Crying.ID, "textures/entity/crying_cat/crying_cat.png");

    public CryingCatEntityRenderer(Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(CatEntityRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public void updateRenderState(CatEntity catEntity, CatEntityRenderState catEntityRenderState, float f) {
        super.updateRenderState(catEntity, catEntityRenderState, f);
        catEntityRenderState.texture = TEXTURE;
   }
}
