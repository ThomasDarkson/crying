package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.interfaces.LightningVars;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;

@Mixin(LightningBolt.class)
public class LightningEntityMixin implements LightningVars {
    Player summoner = null;

    @Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
    private void spawnFire(int spreadAttempts, CallbackInfo info) {
        if (this.get_playerThatSummonedLightningWithCopperCryingTool() != null) {
            info.cancel();
        }
    }

    @Override
    public Player get_playerThatSummonedLightningWithCopperCryingTool() {
        return this.summoner;
    }

    @Override
    public void set_playerThatSummonedLightningWithCopperCryingTool(Player player) {
        this.summoner = player;
    }
}
