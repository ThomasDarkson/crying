package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.interfaces.LightningVars;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "thunderHit", at = @At("HEAD"), cancellable = true)
    public void onStruckByLightning(ServerLevel world, LightningBolt lightningEntity, CallbackInfo info) {
        LightningVars lightning = (LightningVars) lightningEntity;
        Entity entity = (Entity) (Object) this;
        if (entity instanceof Player player) {
            if (lightning.get_playerThatSummonedLightningWithCopperCryingTool() == player) 
                info.cancel();   
        }
    }
}
