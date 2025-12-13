package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.Crying;
import crying.CryingClient;
import crying.interfaces.PlayerEntityRenderStateVarsInterface;
import crying.items.CryingGrapplingHookItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

@Mixin(PlayerRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    public void updateRenderState(AbstractClientPlayer abstractClientPlayerEntity, PlayerRenderState playerEntityRenderState, float f, CallbackInfo info) {
        InteractionHand hand = Crying.getHandThatHasCryingShield(abstractClientPlayerEntity);
        ((PlayerEntityRenderStateVarsInterface) (Object) playerEntityRenderState).set_cryingShieldHand(hand);
        ((PlayerEntityRenderStateVarsInterface) (Object) playerEntityRenderState).set_realAge(abstractClientPlayerEntity.tickCount);
    }

    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void getArmPose(AbstractClientPlayer player, HumanoidArm arm, CallbackInfoReturnable<HumanoidModel.ArmPose> info) {
        InteractionHand hand = Crying.getHandThatHasCryingShield(player);
        if (hand != null && CryingClient.compareHandtoArm(hand, arm, player.getMainArm())) 
            info.setReturnValue(ArmPose.EMPTY);
        else if ((player.getItemHeldByArm(arm).getItem() instanceof CryingGrapplingHookItem))
            info.setReturnValue(ArmPose.CROSSBOW_HOLD);
    }
}
