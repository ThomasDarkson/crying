package crying.tools.mixin;

import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.tools.interfaces.SanityInterface;
import crying.tools.interfaces.SanityManager;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        SanityManager oldManager = ((SanityInterface) (Object) (oldPlayer)).getManagerOverride_crying();
        SanityManager manager = ((SanityInterface) (Object) (player)).getManagerOverride_crying();

        int oldPermantLevel = oldManager.getPermanentMaxLevel();
        manager.setPermanentMaxLevel(oldPermantLevel);
    }
}
