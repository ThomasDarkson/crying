package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import crying.armors.CryingArmor;
import crying.interfaces.FoodVars;
import crying.interfaces.SanityManager;
import crying.interfaces.SanityVars;
import net.minecraft.server.level.ServerPlayer;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "restoreFrom", at = @At("TAIL"))
    public void copyFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo info) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        SanityManager oldManager = Crying.getSanityManager(oldPlayer);
        ((SanityVars) (Object) player).setManager(oldManager);
        SanityManager manager = Crying.getSanityManager(player);
        manager.updateThis();

        int oldPreventTicks = oldManager.getCollapseRegenTicks();
        manager.collapse(oldPreventTicks, player, oldManager.getCollapsingReason());

        FoodVars oldFood = (FoodVars) (Object) oldPlayer;
        FoodVars food = (FoodVars) (Object) player;

        food.setEatenCryingFoodCount(oldFood.getEatenCryingFoodCount());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        SanityManager manager = Crying.getSanityManager(player);
        if (CryingArmor.cryingArmorCount(player) > 0)
            manager.tick(player);
    }
}
