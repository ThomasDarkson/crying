package crying.mixin;

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
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import crying.interfaces.BiomeVars;
import crying.interfaces.CryingTool;
import crying.interfaces.FoodVars;
import crying.interfaces.HookVars;
import crying.interfaces.SanityVars;

@Mixin(Player.class)
public class PlayerEntityMixin implements SanityVars, HookVars, FoodVars, BiomeVars {
    SanityManager SanityManager;
    GrapplingHookEntity hook;
    int eatenCryingFoodCount = 0;
    int coldTicks = 0;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Level world, GameProfile profile, CallbackInfo info) {
        Player player = (Player) (Object) this;
        SanityManager = new SanityManager(player.getStringUUID());
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    public void initDataTracker(SynchedEntityData.Builder builder, CallbackInfo info) {
        builder.define(Crying.FOOD_COUNT, 0);
    }

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    public void damage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        Player player = (Player) (Object) this;
        if (Crying.isTheCriersSword(source.getWeaponItem()) && !(source.getEntity() instanceof CrierEntity)) {
            info.setReturnValue(false);
        }
        if (!player.isInvulnerableTo(world, source) 
            && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
            && !source.is(DamageTypeTags.IS_FALL)
            && !source.is(DamageTypeTags.IS_FIRE)
            && !source.is(DamageTypeTags.IS_DROWNING)
            && !source.is(DamageTypeTags.IS_FREEZING)) {
                if (!source.is(DamageTypeTags.BYPASSES_SHIELD)) {
                    InteractionHand hand = Crying.getHandThatHasCryingShield(player);
                    if (hand != null) {
                        ItemStack stack = player.getItemInHand(hand);
                        if (stack.getItem() instanceof CryingShieldItem item) {
                            item.useShield(stack, hand, player, source.getEntity(), source, Math.round(amount));
                        }
                    }
                }

                if ((source.getEntity() instanceof Monster) || (source.getEntity() instanceof NeutralMob angerable && angerable.getPersistentAngerTarget() != null && angerable.getPersistentAngerTarget().equals(player.getUUID()))) {
                    SanityManager manager = Crying.getSanityManager(player);
                    manager.damage(amount);
                }
        }
        if (player.getMainHandItem().getItem() instanceof CryingTool tool) {
            if (tool.getCoreIngredient() == Items.NETHERITE_INGOT) {
                if (source.is(DamageTypeTags.IS_FIRE)) {
                    player.heal(amount);
                    info.setReturnValue(false);
                }
            }
            else if (tool.getCoreIngredient() == Items.GOLD_INGOT || Crying.isTheCriersSword(player.getMainHandItem())) {
                if (!player.isCreative() && source.getEntity() instanceof Entity && amount > 0F)
                    GranterEntity.summonGranterEntity(world, player);
            }
        }
    }

    @Inject(method = "killedEntity", at = @At("TAIL"), cancellable = true)
    public void onKilledOther(ServerLevel world, LivingEntity other, DamageSource damageSource, CallbackInfoReturnable<Boolean> info) {
        @SuppressWarnings("rawtypes")
        EntityType type = other.getType();
        Player player = (Player) (Object) this;
        SanityManager manager = Crying.getSanityManager(player);

        if (
            type == EntityType.CAT ||
            (other instanceof TamableAnimal && ((TamableAnimal) other).isTame()) ||
            type == EntityType.VILLAGER ||
            type == EntityType.WANDERING_TRADER ||
            type == EntityType.IRON_GOLEM ||
            type == EntityType.SNOW_GOLEM ||
            type == EntityType.ALLAY
        ) {
            manager.collapse(Crying.tickSecond(900), player, CollapsingReason.MURDER);
        }
        else {
            if (manager.getMaxLevel() > 0 && (other instanceof Monster || other instanceof Slime)) {
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

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readCustomData(ValueInput nbt, CallbackInfo info) {
        this.SanityManager.readNbt(nbt);
        this.setEatenCryingFoodCount(nbt.getIntOr("eatenCryingFoodCount", 0));
        this.setTicksInColdBiome(nbt.getIntOr("coldTicks", 0));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    protected void writeCustomData(ValueOutput nbt, CallbackInfo info) {
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
        Player player = (Player) (Object) this;
        try {
            return player.getEntityData().get(Crying.FOOD_COUNT);
        }
        catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void setEatenCryingFoodCount(int count) {
        this.eatenCryingFoodCount = Math.min(count, Crying.MAX_CRYING_FOOD_COUNT);

        Player player = (Player) (Object) this;
        player.getEntityData().set(Crying.FOOD_COUNT, this.eatenCryingFoodCount);
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
