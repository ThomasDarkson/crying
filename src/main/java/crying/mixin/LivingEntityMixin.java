package crying.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.Crying;
import crying.armors.CryingArmor;
import crying.effects.BaneOfCriers;
import crying.interfaces.SanityInterface;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "applyArmorToDamage", at = @At("TAIL"), cancellable = true)
    protected void applyArmorToDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        var entity = (LivingEntity) (Object) this;
        if (CryingArmor.setCount(entity) > 0 && !source.isIn(DamageTypeTags.BYPASSES_RESISTANCE)) {
            float dmg = info.getReturnValue();

            float i = CryingArmor.setCount(entity) * 5F;
            if (entity instanceof PlayerEntity player) {
                SanityInterface cryingPlayer = (SanityInterface) (Object) player;
                i = i * (cryingPlayer.getManagerOverride_crying().getSanityLevel() / (float) cryingPlayer.getManagerOverride_crying().getMaxLevel());
                i *= 4F;
            }

            if (i > 80F)
                i = 80F;
            info.setReturnValue(dmg - (dmg * (i / 100F)));
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
                    if (!player.isInvulnerableTo((ServerWorld) player.getWorld(), source) 
                    && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)
                    && !source.isIn(DamageTypeTags.IS_FALL)
                    && !source.isIn(DamageTypeTags.IS_FIRE)
                    && !source.isIn(DamageTypeTags.IS_DROWNING)
                    && !source.isIn(DamageTypeTags.IS_FREEZING)) {
                        if (!source.isIn(DamageTypeTags.BYPASSES_SHIELD)) {
                            if (player.isSneaking())
                                dmg = 0;
                            else
                                dmg = dmg * 0.75F;

                            info.setReturnValue(dmg);
                        }
                    }
                }
            }
        }
    }
}
