package crying.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
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
import crying.entities.GrapplingHookEntity;
import crying.enums.CollapsingReason;
import crying.interfaces.SanityManager;
import crying.tools.CryingShieldItem;
import crying.interfaces.BiomeVars;
import crying.interfaces.CryingTool;
import crying.interfaces.FoodVars;
import crying.interfaces.HookVars;
import crying.interfaces.SanityVars;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements SanityVars, HookVars, FoodVars, BiomeVars {
    SanityManager SanityManager;
    GrapplingHookEntity hook;
    int eatenCryingFoodCount = 0;
    int coldTicks = 0;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        SanityManager = new SanityManager(player.getUuidAsString());
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(DataTracker.Builder builder, CallbackInfo info) {
        builder.add(Crying.FOOD_COUNT, 0);
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
            && !source.isIn(DamageTypeTags.IS_FREEZING)) {
                if (!source.isIn(DamageTypeTags.BYPASSES_SHIELD)) {
                    Hand hand = Crying.getHandThatHasCryingShield(player);
                    if (hand != null) {
                        ItemStack stack = player.getStackInHand(hand);
                        if (stack.getItem() instanceof CryingShieldItem item) {
                            item.useShield(stack, hand, player, source.getAttacker(), source, Math.round(amount));
                        }
                    }
                }

                if ((source.getAttacker() instanceof HostileEntity) || (source.getAttacker() instanceof Angerable angerable && angerable.getAngryAt() != null && angerable.getAngryAt().equals(player.getUuid()))) {
                    SanityManager manager = Crying.getSanityManager(player);
                    manager.damage(amount);
                }
        }
        if (player.getMainHandStack().getItem() instanceof CryingTool tool) {
            if (tool.getCoreIngredient() == Items.NETHERITE_INGOT) {
                if (source.isIn(DamageTypeTags.IS_FIRE)) {
                    player.heal(amount);
                    info.setReturnValue(false);
                }
            }
            else if (tool.getCoreIngredient() == Items.GOLD_INGOT || Crying.isTheCriersSword(player.getMainHandStack())) {
                if (!player.isCreative() && source.getAttacker() instanceof Entity)
                    GranterEntity.summonGranterEntity(world, player);
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        var player = (PlayerEntity) (Object) this;
        if (player instanceof ServerPlayerEntity serverPlayer && Crying.getSanityManager(serverPlayer).getMaxLevel() > 0) {
            this.SanityManager.update(serverPlayer);
        }
    }

    @Inject(method = "onKilledOther", at = @At("TAIL"), cancellable = true)
    public void onKilledOther(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> info) {
        @SuppressWarnings("rawtypes")
        EntityType type = other.getType();
        PlayerEntity player = (PlayerEntity) (Object) this;
        SanityManager manager = Crying.getSanityManager(player);

        if (
            type == EntityType.CAT ||
            (other instanceof TameableEntity && ((TameableEntity) other).isTamed()) ||
            type == EntityType.VILLAGER ||
            type == EntityType.WANDERING_TRADER ||
            type == EntityType.IRON_GOLEM ||
            type == EntityType.SNOW_GOLEM ||
            type == EntityType.ALLAY
        ) {
            manager.collapse(Crying.tickSecond(900), player, CollapsingReason.MURDER);
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

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        this.SanityManager.readNbt(nbt);
        this.setEatenCryingFoodCount(nbt.getInt("eatenCryingFoodCount", 0));
        this.setTicksInColdBiome(nbt.getInt("coldTicks", 0));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        this.SanityManager.writeNbt(nbt);
        nbt.putInt("eatenCryingFoodCount", this.getEatenCryingFoodCount());
        nbt.putInt("coldTicks", this.getTicksInColdBiome());
    }

    @Override
    public void setManager(SanityManager manager) {
        this.SanityManager = manager;
    }

    @Override
    public SanityManager getManager() {
        return this.SanityManager;
    }

    @Override
    public GrapplingHookEntity getHook() {
        return this.hook;
    }

    @Override
    public void setHook(GrapplingHookEntity entity) {
        this.hook = entity;
    }

    @Override
    public int getEatenCryingFoodCount() {
        PlayerEntity player = (PlayerEntity) (Object) this;
        try {
            return player.getDataTracker().get(Crying.FOOD_COUNT);
        }
        catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void setEatenCryingFoodCount(int count) {
        this.eatenCryingFoodCount = Math.min(count, Crying.MAX_CRYING_FOOD_COUNT);

        PlayerEntity player = (PlayerEntity) (Object) this;
        player.getDataTracker().set(Crying.FOOD_COUNT, this.eatenCryingFoodCount);
    }

    @Override
    public int getTicksInColdBiome() {
        return coldTicks;
    }

    @Override
    public void setTicksInColdBiome(int tick) {
        this.coldTicks = tick;
    }
}
