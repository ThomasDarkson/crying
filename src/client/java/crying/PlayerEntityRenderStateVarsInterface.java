package crying;

import net.minecraft.util.Hand;

public interface PlayerEntityRenderStateVarsInterface {
    Hand get_cryingShieldHand();
    void set_cryingShieldHand(Hand hand);

    int get_realAge();
    void set_realAge(int age);
}
