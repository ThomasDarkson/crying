package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.interfaces.LightningVars;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(LightningEntity.class)
public class LightningEntityMixin implements LightningVars {
    PlayerEntity summoner = null;

    @Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
    private void spawnFire(int spreadAttempts, CallbackInfo info) {
        if (this.get_playerThatSummonedLightningWithCopperCryingTool() != null) {
            info.cancel();
        }
    }

    @Override
    public PlayerEntity get_playerThatSummonedLightningWithCopperCryingTool() {
        return this.summoner;
    }

    @Override
    public void set_playerThatSummonedLightningWithCopperCryingTool(PlayerEntity player) {
        this.summoner = player;
    }
}
