package crying.states;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;

public class CriersHeartBlockEntityRenderState extends BlockEntityRenderState {
    public float heartbeatPhase = 0.0f;
    public float SPEED = 5.5F;
    public float speedModifier = 0;
    public float tickProgress = 0;
    public BlockState state;
}
