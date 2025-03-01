package crying.tools.entities;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;
import net.minecraft.util.Identifier;
import crying.tools.Crying;
import net.minecraft.client.render.entity.ZombieEntityRenderer;

public class CrierEntityRenderer extends ZombieEntityRenderer {
    private static final Identifier TEXTURE = Identifier.of(Crying.ID, "textures/entity/crier/crier.png");

    public CrierEntityRenderer(Context context) {
        super(context);
    }

    @Override
    public ZombieEntityRenderState createRenderState() {
        ZombieEntityRenderState state = new ZombieEntityRenderState();
        return state;
    }

    @Override
    public Identifier getTexture(ZombieEntityRenderState renderState) {
        return TEXTURE;
    }

    @Override
    protected boolean isShaking(ZombieEntityRenderState zombieEntityRenderState) {
        return false;
    }
}
