package crying.entities.crier;

import net.minecraft.client.render.entity.state.BipedEntityRenderState;

public class CrierEntityRenderState extends BipedEntityRenderState {
    public boolean forlorn = false;
    public float shieldHealth = 0;
    public int realAge = 0;

    public float headSpinDegrees;
    public float headFloatOffset;
    
    public CrierEntityRenderState() {
    }
}