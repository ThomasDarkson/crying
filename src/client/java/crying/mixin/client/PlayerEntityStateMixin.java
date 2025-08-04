package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import crying.PlayerEntityRenderStateVarsInterface;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.util.Hand;

@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityStateMixin implements PlayerEntityRenderStateVarsInterface {
    Hand cryingShieldHand = null;
    int realAge = 0;

    @Override
    public Hand get_cryingShieldHand() {
        return this.cryingShieldHand;
    }

    @Override
    public void set_cryingShieldHand(Hand hand) {
        this.cryingShieldHand = hand;
    }

    @Override
    public int get_realAge() {
        return this.realAge;
    }

    @Override
    public void set_realAge(int age) {
        this.realAge = age;
    }
}
