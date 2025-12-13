package crying.states;

import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.world.phys.Vec3;

public class GrapplingHookEntityRenderState extends ArrowRenderState {
    public Vec3 hookOffset;
    public Vec3 startHookPos;
    public Vec3 endHookPos;
}
