package crying.tools.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.tools.Crying;
import crying.tools.armors.CryingArmor;
import crying.tools.effects.BaneOfCriers;
import crying.tools.effects.LoveOfTheFeline;
import crying.tools.enchantments.Aegis;
import crying.tools.enchantments.Bloodlust;
import crying.tools.enchantments.Feathered;
import crying.tools.interfaces.SanityInterface;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        LivingEntity entity = (LivingEntity) (Object) this;
        ItemStack weapon = source.getWeaponStack();
        if (weapon != null && weapon.getItem() == Crying.THE_CRYING_BEING && (entity instanceof PlayerEntity)) {
            info.setReturnValue(false);
        } 
        if (entity.hasStatusEffect(LoveOfTheFeline.LOVE_OF_THE_FELINE) && source.isIn(DamageTypeTags.IS_FALL)) {
            info.setReturnValue(false);
        }
        ItemStack chestplate = entity.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack main = entity.getMainHandStack();
        int level = EnchantmentHelper.getLevel(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Aegis.AEGIS), chestplate);
        if (level > 0) {
            if (source.isIn(DamageTypeTags.IS_PROJECTILE) || source.isIn(DamageTypeTags.IS_EXPLOSION) || source.isIn(DamageTypeTags.IS_LIGHTNING)) {
                world.playSound((PlayerEntity) null, entity.getX(), entity.getY(), entity.getZ(), source.isIn(DamageTypeTags.IS_FALL) ? SoundEvents.ENTITY_GENERIC_SPLASH : SoundEvents.ITEM_SHIELD_BLOCK, entity.getSoundCategory(), 1.0F, 1.0F);
                info.setReturnValue(false);
            }
        }

        int mainl = EnchantmentHelper.getLevel(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Feathered.FEATHERED), main);
        if (mainl > 0) {
            if (source.isIn(DamageTypeTags.IS_FALL) || source.isOf(DamageTypes.FLY_INTO_WALL)) {
                info.setReturnValue(false);
            }
        }
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    public void onDeath(DamageSource source, CallbackInfo info) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (source.getAttacker() != null && (source.getAttacker() instanceof PlayerEntity player) && (entity instanceof HostileEntity || entity instanceof SlimeEntity)) {
            ItemStack leggings = player.getEquippedStack(EquipmentSlot.LEGS);
            World world = player.getWorld();
            if (leggings != null) {
                int l = EnchantmentHelper.getLevel(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Bloodlust.BLOODLUST), leggings);
                if (l > 0) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, l * 40, 3, false, false, true));
                }
            }
        }
    }

    @Inject(method = "modifyAppliedDamage", at = @At("TAIL"), cancellable = true)
    protected void modifyDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        if (!source.isIn(DamageTypeTags.BYPASSES_EFFECTS)) {
            var entity = (LivingEntity) (Object) this;
            float dmg = info.getReturnValue();
            if (entity.hasStatusEffect(BaneOfCriers.EFFECT))
                dmg = dmg * 2F;
            
            if (entity.hasStatusEffect(LoveOfTheFeline.LOVE_OF_THE_FELINE))
                dmg = dmg - (dmg * 0.8F);

            if (CryingArmor.setCount(entity) > 0 && !source.isIn(DamageTypeTags.BYPASSES_RESISTANCE)) {
                float i = ((float) CryingArmor.setCount(entity)) * 5F;
                if (entity instanceof PlayerEntity player) {
                    SanityInterface cryingPlayer = (SanityInterface) (Object) player;
                    i = i * (cryingPlayer.getManagerOverride_crying().getsanityLevel() / (float) cryingPlayer.getManagerOverride_crying().getMaxLevel());
                }
                else
                    i = 0;
                float j = 25 - i;
                float f = dmg * j;
                float g = dmg;
                dmg = Math.max(f / 25.0F, 0.0F);
                float h = g - dmg;
                if (h > 0.0F && h < 3.4028235E37F) {
                    if (entity instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity)entity).increaseStat(Stats.DAMAGE_RESISTED, Math.round(h * 10.0F));
                    } else if (source.getAttacker() instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity)source.getAttacker()).increaseStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(h * 10.0F));
                    }
                }

                info.setReturnValue(dmg);
            }
            if (source.getWeaponStack() != null) {
                Item weapon = source.getWeaponStack().getItem();
                if ((weapon == Crying.sword || weapon == Crying.crying_knife) && (
                    entity.getType() == EntityType.ENDERMAN ||
                    entity.getType() == EntityType.ENDERMITE ||
                    entity.getType() == EntityType.GHAST
                )) {
                    info.setReturnValue(dmg + 20F);
                }
            }

            info.setReturnValue(dmg);
        }
    }
}
