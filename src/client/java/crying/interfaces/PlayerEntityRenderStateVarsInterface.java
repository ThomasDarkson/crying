package crying.interfaces;

import net.minecraft.util.Hand;

public interface PlayerEntityRenderStateVarsInterface extends EntityRenderStateVarsInterface {
    Hand get_cryingShieldHand();
    void set_cryingShieldHand(Hand hand);
}
