package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import crying.interfaces.PlayerEntityRenderStateVarsInterface;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

@Mixin(PlayerRenderState.class)
public class PlayerEntityStateMixin implements PlayerEntityRenderStateVarsInterface {
    InteractionHand cryingShieldHand = null;
    int realAge = 0;
    HumanoidArm isUsingAscender = null;

    @Override
    public InteractionHand get_cryingShieldHand() {
        return this.cryingShieldHand;
    }

    @Override
    public void set_cryingShieldHand(InteractionHand hand) {
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
