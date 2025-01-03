package crying.tools.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import crying.tools.interfaces.CryingInterface;
import crying.tools.interfaces.CryingManager;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements CryingInterface {
    CryingManager cryingManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        cryingManager = new CryingManager(player.getUuidAsString());
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!player.isInvulnerableTo(world, source) 
            && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)
            && !source.isIn(DamageTypeTags.IS_FALL)
            && !source.isIn(DamageTypeTags.IS_FIRE)
            && !source.isIn(DamageTypeTags.IS_DROWNING)
            && !source.isIn(DamageTypeTags.IS_FREEZING)
            && (source.getAttacker() instanceof HostileEntity)
            ) {
                CryingManager manager = ((CryingInterface) (Object) player).getManagerOverride_crying();
                manager.damage(amount);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        var player = (PlayerEntity) (Object) this;
        if (player instanceof ServerPlayerEntity serverPlayer) {
            this.cryingManager.update(serverPlayer);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        this.cryingManager.readNbt(nbt);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        this.cryingManager.writeNbt(nbt);
    }

    @Override
    public void setManagerOverride_crying(CryingManager manager) {
        this.cryingManager = manager;
    }

    @Override
    public CryingManager getManagerOverride_crying() {
        return this.cryingManager;
    }
}
