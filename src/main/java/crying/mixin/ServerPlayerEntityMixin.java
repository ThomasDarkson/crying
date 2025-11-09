package crying.mixin;

import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import crying.armors.CryingArmor;
import crying.interfaces.FoodVars;
import crying.interfaces.SanityManager;
import crying.interfaces.SanityVars;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
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
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        SanityManager manager = Crying.getSanityManager(player);
        if (CryingArmor.cryingArmorCount(player) > 0)
            manager.tick(player);
    }
}
