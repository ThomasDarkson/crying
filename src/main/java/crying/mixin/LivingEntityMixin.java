package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.Crying;
import crying.armors.CryingArmor;
import crying.effects.BaneOfCriers;
import crying.interfaces.LightningVars;
import crying.interfaces.OxidizableCryingTool;
import crying.interfaces.SanityManager;
import crying.items.CryingFoodItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow
    boolean jumping;

    @Inject(method = "hurtServer", at = @At("TAIL"), cancellable = true)
    public void damage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        LivingEntity thisEntity = (LivingEntity) (Object) this;
        Entity attacker = source.getEntity();

        if (!(thisEntity instanceof Player) && (attacker instanceof ServerPlayer player)) {
            ItemStack stack = source.getWeaponItem();
            if (stack != null && stack.getItem() instanceof OxidizableCryingTool tool) {
                if (tool.getCoreIngredient() == Items.COPPER_INGOT) {
                    WeatherState level = tool.getOxidationLevel(stack);
                    float chance = 1F;
                    if (level == WeatherState.EXPOSED) 
                        chance = 0.75F;
                    if (level == WeatherState.WEATHERED) 
                        chance = 0.5F;
                    if (level == WeatherState.OXIDIZED) 
                        chance = 0.25F;
                    
                    if (world.getRandom().nextFloat() < chance) {
                        LightningBolt entity = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
                        entity.setCause(player);
                        ((LightningVars) entity).set_playerThatSummonedLightningWithCopperCryingTool(player);
                        entity.setPos(thisEntity.position());
                        world.addFreshEntity(entity);
                    }
                }
            }
        }
    }

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At("TAIL"), cancellable = true)
    protected void applyArmorToDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        var entity = (LivingEntity) (Object) this;
        if (CryingArmor.setCount(entity) > 0 && !source.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            float dmg = info.getReturnValue();
            float extraProtection = 0F;

            float i = CryingArmor.setCount(entity) * 5F;
            if (entity instanceof Player player) {
                SanityManager manager = Crying.getSanityManager(player);
                i = i * (manager.getSanityLevel() / (float) manager.getMaxLevel());
                i *= 4F;
                extraProtection = CryingFoodItem.getExtraProtectionFormula(player);
            }

            if (i > 80F)
                i = 80F;
            float rDmg = dmg - (dmg * (i / 100F));
            info.setReturnValue(rDmg * (1F - extraProtection));
        }
    }

    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("TAIL"), cancellable = true)
    protected void modifyAppliedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        if (!source.is(DamageTypeTags.BYPASSES_EFFECTS)) {
            var entity = (LivingEntity) (Object) this;
            float dmg = info.getReturnValue();
            if (entity.hasEffect(BaneOfCriers.BANE_OF_CRIERS)) {
                info.setReturnValue(dmg * 2F);
            }

            if (entity instanceof Player player) {
                InteractionHand hand = Crying.getHandThatHasCryingShield(player);
                if (hand != null) {
                    if (!player.isInvulnerableTo((ServerLevel) player.level(), source) 
                    && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                    && !source.is(DamageTypeTags.IS_FALL)
                    && !source.is(DamageTypeTags.IS_FIRE)
                    && !source.is(DamageTypeTags.IS_DROWNING)
                    && !source.is(DamageTypeTags.IS_FREEZING)) {
                        if (!source.is(DamageTypeTags.BYPASSES_SHIELD)) {
                            dmg = dmg * (player.isShiftKeyDown() ? 0.1F : 0.75F);
                            info.setReturnValue(dmg);
                        }
                    }
                }
            }
        }
    }
}
