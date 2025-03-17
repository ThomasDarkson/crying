package crying.tools.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.tools.Crying;
import crying.tools.goals.ActivePlayerTargetGoal;

import java.util.function.Predicate;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;

@Mixin(MobEntity.class)
public class CreeperEntityMixin {
    @Final
	@Mutable
	@Shadow
	private GoalSelector goalSelector;

    @Final
	@Mutable
	@Shadow
	private GoalSelector targetSelector;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Inject(method = "<init>", at = @At("TAIL"))
    public void initGoals(CallbackInfo info) {
        MobEntity mob = (MobEntity) (Object) this;
        if (mob instanceof CreeperEntity creeper) {
            this.targetSelector.clear((goal) -> {
                return goal.getClass() == ActiveTargetGoal.class;
            });
            this.targetSelector.add(1, new ActivePlayerTargetGoal(creeper, PlayerEntity.class, true));

            Predicate<LivingEntity> predicate = (entity) -> {
                if (entity instanceof PlayerEntity player) {
                    return player.getEquippedStack(EquipmentSlot.HEAD).getItem() == Crying.crying_cat_item;
                }
                return false;
            };
            goalSelector.add(1, new FleeEntityGoal(creeper, PlayerEntity.class, predicate, 10.0F, 1.2, 1.5, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR));
        }
    }
}
