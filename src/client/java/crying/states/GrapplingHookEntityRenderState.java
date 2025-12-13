package crying.states;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

public class GrapplingHookEntityRenderState extends EntityRenderState {
    public Vec3 hookOffset;
    public Vec3 startHookPos;
    public Vec3 endHookPos;
}
