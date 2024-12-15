package crying.tools.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.tools.Crying;
import crying.tools.other.Crier;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "modifyAppliedDamage", at = @At("TAIL"), cancellable = true)
    protected void modifyDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        if (!source.isIn(DamageTypeTags.BYPASSES_EFFECTS)) {
            var entity = (LivingEntity) (Object) this;
            float dmg = info.getReturnValue();
            if (entity.hasStatusEffect(Crier.CRIER) && !source.isIn(DamageTypeTags.BYPASSES_RESISTANCE)) {
                Crier crier = (Crier) (entity.getStatusEffect(Crier.CRIER).getEffectType().value());
                int i = (crier.getAmplifier() + 1) * 5;
                Crying.LOGGER.info("Amplifier " + crier.getAmplifier());
                int j = 25 - i;
                float f = dmg * (float)j;
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
                if (source.getWeaponStack().getItem() == Crying.sword && (
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
