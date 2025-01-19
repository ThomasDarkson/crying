package crying.tools.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.tools.Crying;
import crying.tools.effects.BaneOfCriers;
import crying.tools.effects.Crier;
import crying.tools.interfaces.CryingInterface;
import crying.tools.interfaces.CryingManager;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "modifyAppliedDamage", at = @At("TAIL"), cancellable = true)
    protected void modifyDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        if (!source.isIn(DamageTypeTags.BYPASSES_EFFECTS)) {
            var entity = (LivingEntity) (Object) this;
            float dmg = info.getReturnValue();
            if (entity.hasStatusEffect(Crier.CRIER) && !source.isIn(DamageTypeTags.BYPASSES_RESISTANCE)) {
                Crier crier = (Crier) (entity.getStatusEffect(Crier.CRIER).getEffectType().value());
                float i = ((float) crier.getAmplifier() + 1F) * 5F;
                if (entity instanceof PlayerEntity player) {
                    CryingInterface cryingPlayer = (CryingInterface) (Object) player;
                    i = i * (cryingPlayer.getManagerOverride_crying().getCryingLevel() / (float) cryingPlayer.getManagerOverride_crying().getMaxLevel());
                }
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
                if (source.getWeaponStack().getItem() == Crying.sword && (
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
            if (!(entity instanceof PlayerEntity)) {
                if (source.getAttacker() instanceof PlayerEntity player) {
                    CryingManager manager = ((CryingInterface) (Object) player).getManagerOverride_crying();
                    if (manager.getMaxLevel() > 0) {
                        if (entity.getHealth() - dmg <= 0F && (entity instanceof HostileEntity)) {
                            manager.decreaseLevel(-1F);
                        } 
                    }
                }
            }
        }
    }
}
