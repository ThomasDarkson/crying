package crying.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.armors.CryingArmor;
import crying.effects.BaneOfCriers;
import crying.interfaces.SanityInterface;
import crying.tools.sword.AbstractCryingSwordItem;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "modifyAppliedDamage", at = @At("TAIL"), cancellable = true)
    protected void modifyDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        if (!source.isIn(DamageTypeTags.BYPASSES_EFFECTS)) {
            var entity = (LivingEntity) (Object) this;
            float dmg = info.getReturnValue();
            if (entity.hasStatusEffect(BaneOfCriers.BANE_OF_CRIERS)) {
                dmg = dmg * 2F;
                info.setReturnValue(dmg);
            }

            if (CryingArmor.setCount(entity) > 0 && !source.isIn(DamageTypeTags.BYPASSES_RESISTANCE)) {
                float i = ((float) CryingArmor.setCount(entity)) * 5F;
                if (entity instanceof PlayerEntity player) {
                    SanityInterface cryingPlayer = (SanityInterface) (Object) player;
                    i = i * (cryingPlayer.getManagerOverride_crying().getSanityLevel() / (float) cryingPlayer.getManagerOverride_crying().getMaxLevel());
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
                if ((weapon instanceof AbstractCryingSwordItem) && (
                    entity.getType() == EntityType.ENDERMAN ||
                    entity.getType() == EntityType.ENDERMITE ||
                    entity.getType() == EntityType.GHAST
                )) {
                    info.setReturnValue(dmg + 20F);
                }
            }
        }
    }
}
