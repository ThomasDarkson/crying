package crying.interfaces;

import net.minecraft.world.InteractionHand;

public interface PlayerEntityRenderStateVarsInterface extends EntityRenderStateVarsInterface {
    InteractionHand get_cryingShieldHand();
    void set_cryingShieldHand(InteractionHand hand);
}
