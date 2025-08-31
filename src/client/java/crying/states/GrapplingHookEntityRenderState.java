package crying.states;

import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.math.Vec3d;

public class GrapplingHookEntityRenderState extends ProjectileEntityRenderState {
    public Vec3d hookOffset;
    public Vec3d startHookPos;
    public Vec3d endHookPos;
}
