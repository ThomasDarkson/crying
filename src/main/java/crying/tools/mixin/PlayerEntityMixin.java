package crying.tools.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.TameableEntity;
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

import crying.tools.Crying;
import crying.tools.effects.LoveOfTheFeline;
import crying.tools.interfaces.SanityManager;
import crying.tools.interfaces.SanityInterface;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements SanityInterface {
    SanityManager SanityManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        SanityManager = new SanityManager(player.getUuidAsString());
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
            && player.getMainHandStack().getItem() != Crying.THE_CRYING_BEING
            && ((source.getAttacker() instanceof HostileEntity) || (source.getAttacker() instanceof Angerable))
            ) {
                SanityManager manager = ((SanityInterface) (Object) player).getManagerOverride_crying();
                manager.damage(amount);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        var player = (PlayerEntity) (Object) this;
        if (player instanceof ServerPlayerEntity serverPlayer) {
            this.SanityManager.update(serverPlayer);
        }
    }

    @Inject(method = "onKilledOther", at = @At("TAIL"), cancellable = true)
    public void onKilledOther(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> info) {
        @SuppressWarnings("rawtypes")
        EntityType type = other.getType();
        PlayerEntity player = (PlayerEntity) (Object) this;
        SanityManager manager = ((SanityInterface) (Object) player).getManagerOverride_crying();

        if (
            type == EntityType.CAT ||
            (other instanceof TameableEntity && ((TameableEntity) other).isTamed()) ||
            type == EntityType.VILLAGER ||
            type == EntityType.WANDERING_TRADER ||
            type == EntityType.IRON_GOLEM ||
            type == EntityType.SNOW_GOLEM ||
            type == EntityType.ALLAY
        ) {
            manager.decreaseLevel(20F);
        }
        else {
            if (manager.getMaxLevel() > 0 && (other instanceof HostileEntity || other instanceof SlimeEntity)) {
                float decreaseValue = -1F;
                if (type == EntityType.GHAST || type == EntityType.ENDERMAN || type == EntityType.ENDERMITE)
                    decreaseValue = -5F;
                else if (type == EntityType.EVOKER)
                    decreaseValue = -4F;
                else if (type == EntityType.WITCH)
                    decreaseValue = -8F;
                else if (type == EntityType.ILLUSIONER)
                    decreaseValue = -16F;

                manager.decreaseLevel(type == Crying.CRIER ? -20F : decreaseValue);
            }
        }
    }

    @Inject(method = "wakeUp", at = @At("TAIL"))
    public void wakeUp(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.hasStatusEffect(LoveOfTheFeline.LOVE_OF_THE_FELINE)) {
            player.setHealth(player.getMaxHealth());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        this.SanityManager.readNbt(nbt);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        this.SanityManager.writeNbt(nbt);
    }

    @Override
    public void setManagerOverride_crying(SanityManager manager) {
        this.SanityManager = manager;
    }

    @Override
    public SanityManager getManagerOverride_crying() {
        return this.SanityManager;
    }
}
