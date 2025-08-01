package crying.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import crying.Crying;
import crying.entities.CrierEntity;
import crying.entities.GranterEntity;
import crying.interfaces.SanityManager;
import crying.interfaces.CryingTool;
import crying.interfaces.SanityInterface;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements SanityInterface {
    SanityManager SanityManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(World world, GameProfile profile, CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        SanityManager = new SanityManager(player.getUuidAsString());
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (Crying.isTheCriersSword(source.getWeaponStack()) && !(source.getAttacker() instanceof CrierEntity)) {
            info.setReturnValue(false);
        }
        if (!player.isInvulnerableTo(world, source) 
            && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)
            && !source.isIn(DamageTypeTags.IS_FALL)
            && !source.isIn(DamageTypeTags.IS_FIRE)
            && !source.isIn(DamageTypeTags.IS_DROWNING)
            && !source.isIn(DamageTypeTags.IS_FREEZING)
            /*&& player.getMainHandStack().getItem() != Crying.THE_CRYING_BEING*/
            && ((source.getAttacker() instanceof HostileEntity) || (source.getAttacker() instanceof Angerable))
            ) {
                SanityManager manager = ((SanityInterface) (Object) player).getManagerOverride_crying();
                manager.damage(amount);
        }
        if (player.getMainHandStack().getItem() instanceof CryingTool tool) {
            if (tool.getCoreIngredient() == Items.NETHERITE_INGOT) {
                if (source.isIn(DamageTypeTags.IS_FIRE)) {
                    player.heal(amount);
                    info.setReturnValue(false);
                }
            }
            else if (tool.getCoreIngredient() == Items.GOLD_INGOT || Crying.isTheCriersSword(player.getMainHandStack())) {
                GranterEntity.summonGranterEntity(world, player);
            }
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
            manager.setPreventRegenTicks(Crying.tickSecond(900), player);
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

    @Inject(method = "readCustomData", at = @At("TAIL"))
    public void readCustomData(ReadView nbt, CallbackInfo info) {
        this.SanityManager.readNbt(nbt);
    }

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    protected void writeCustomData(WriteView nbt, CallbackInfo info) {
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
