package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.interfaces.LightningVars;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "onStruckByLightning", at = @At("HEAD"), cancellable = true)
    public void onStruckByLightning(ServerWorld world, LightningEntity lightningEntity, CallbackInfo info) {
        LightningVars lightning = (LightningVars) lightningEntity;
        Entity entity = (Entity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            if (lightning.get_playerThatSummonedLightningWithCopperCryingTool() == player) 
                info.cancel();   
        }
    }
}
