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
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "updateRenderState", at = @At("TAIL"))
    public void updateRenderState(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo info) {
        Hand hand = Crying.getHandThatHasCryingShield(abstractClientPlayerEntity);
        ((PlayerEntityRenderStateVarsInterface) (Object) playerEntityRenderState).set_cryingShieldHand(hand);
        ((PlayerEntityRenderStateVarsInterface) (Object) playerEntityRenderState).set_realAge(abstractClientPlayerEntity.age);
    }

    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void getArmPose(AbstractClientPlayerEntity player, Arm arm, CallbackInfoReturnable<BipedEntityModel.ArmPose> info) {
        Hand hand = Crying.getHandThatHasCryingShield(player);
        if (hand != null && CryingClient.compareHandtoArm(hand, arm, player.getMainArm())) 
            info.setReturnValue(ArmPose.EMPTY);
        else if ((player.getStackInArm(arm).getItem() instanceof CryingGrapplingHookItem))
            info.setReturnValue(ArmPose.CROSSBOW_HOLD);
    }
}
