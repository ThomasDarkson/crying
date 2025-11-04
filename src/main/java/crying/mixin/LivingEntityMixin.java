package crying.mixin;

import net.minecraft.block.Oxidizable.OxidationLevel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
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

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow
    boolean jumping;

    @Inject(method = "damage", at = @At("TAIL"), cancellable = true)
    public void damage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        LivingEntity thisEntity = (LivingEntity) (Object) this;
        Entity attacker = source.getAttacker();

        if (!(thisEntity instanceof PlayerEntity) && (attacker instanceof ServerPlayerEntity player)) {
            ItemStack stack = source.getWeaponStack();
            if (stack != null && stack.getItem() instanceof OxidizableCryingTool tool) {
                if (tool.getCoreIngredient() == Items.COPPER_INGOT) {
                    OxidationLevel level = tool.getOxidationLevel(stack);
                    float chance = 1F;
                    if (level == OxidationLevel.EXPOSED) 
                        chance = 0.75F;
                    if (level == OxidationLevel.WEATHERED) 
                        chance = 0.5F;
                    if (level == OxidationLevel.OXIDIZED) 
                        chance = 0.25F;
                    
                    if (world.getRandom().nextFloat() < chance) {
                        LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                        entity.setChanneler(player);
                        ((LightningVars) entity).set_playerThatSummonedLightningWithCopperCryingTool(player);
                        entity.setPosition(thisEntity.getEntityPos());
                        world.spawnEntity(entity);
                    }
                }
            }
        }
    }

    @Inject(method = "applyArmorToDamage", at = @At("TAIL"), cancellable = true)
    protected void applyArmorToDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        var entity = (LivingEntity) (Object) this;
        if (CryingArmor.setCount(entity) > 0 && !source.isIn(DamageTypeTags.BYPASSES_RESISTANCE)) {
            float dmg = info.getReturnValue();
            float extraProtection = 0F;

            float i = CryingArmor.setCount(entity) * 5F;
            if (entity instanceof PlayerEntity player) {
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

    @Inject(method = "modifyAppliedDamage", at = @At("TAIL"), cancellable = true)
    protected void modifyAppliedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        if (!source.isIn(DamageTypeTags.BYPASSES_EFFECTS)) {
            var entity = (LivingEntity) (Object) this;
            float dmg = info.getReturnValue();
            if (entity.hasStatusEffect(BaneOfCriers.BANE_OF_CRIERS)) {
                info.setReturnValue(dmg * 2F);
            }

            if (entity instanceof PlayerEntity player) {
                Hand hand = Crying.getHandThatHasCryingShield(player);
                if (hand != null) {
                    if (!player.isInvulnerableTo((ServerWorld) player.getEntityWorld(), source) 
                    && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)
                    && !source.isIn(DamageTypeTags.IS_FALL)
                    && !source.isIn(DamageTypeTags.IS_FIRE)
                    && !source.isIn(DamageTypeTags.IS_DROWNING)
                    && !source.isIn(DamageTypeTags.IS_FREEZING)) {
                        if (!source.isIn(DamageTypeTags.BYPASSES_SHIELD)) {
                            dmg = dmg * (player.isSneaking() ? 0.1F : 0.75F);
                            info.setReturnValue(dmg);
                        }
                    }
                }
            }
        }
    }
}
