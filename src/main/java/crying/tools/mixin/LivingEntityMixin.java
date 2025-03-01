package crying.tools.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.tools.Crying;
import crying.tools.armors.CryingArmor;
import crying.tools.effects.BaneOfCriers;
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
        if (entity.getMainHandStack().getItem() == Crying.THE_CRYING_BEING) {
            if (source.isIn(DamageTypeTags.IS_PROJECTILE) || source.isIn(DamageTypeTags.IS_EXPLOSION) || source.isIn(DamageTypeTags.IS_LIGHTNING) || source.isIn(DamageTypeTags.IS_FALL)) {
                world.playSound((PlayerEntity) null, entity.getX(), entity.getY(), entity.getZ(), source.isIn(DamageTypeTags.IS_FALL) ? SoundEvents.ENTITY_GENERIC_SPLASH : SoundEvents.ITEM_SHIELD_BLOCK, entity.getSoundCategory(), 1.0F, 1.0F);
                info.setReturnValue(false);
            }
        }
    }

    @Inject(method = "modifyAppliedDamage", at = @At("TAIL"), cancellable = true)
    protected void modifyDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        if (!source.isIn(DamageTypeTags.BYPASSES_EFFECTS)) {
            var entity = (LivingEntity) (Object) this;
            float dmg = info.getReturnValue();
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
            if (entity.hasStatusEffect(BaneOfCriers.EFFECT)) {
                info.setReturnValue(dmg * 2F);
            }
        }
    }
}
