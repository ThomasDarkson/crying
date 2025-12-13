package crying.entities.crier;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class CrierEntityRenderState extends HumanoidRenderState {
    public boolean forlorn = false;
    public float shieldHealth = 0;
    public int realAge = 0;

    public float headSpinDegrees;
    public float headFloatOffset;
    
    public CrierEntityRenderState() {
    }
}