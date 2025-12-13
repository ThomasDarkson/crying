package crying.goals;

import java.util.function.Predicate;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;

public class FastBreakDoorGoal extends DoorInteractGoal {
    protected int breakProgress;
    protected int prevBreakProgress;
    protected int maxProgress;

    public FastBreakDoorGoal(Mob mob, Predicate<Difficulty> difficultySufficientPredicate) {
        super(mob);
        this.prevBreakProgress = -1;
        this.maxProgress = -1;
    }
    
    protected int getMaxProgress() {
        return Math.max(240, this.maxProgress);
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) {
            return false;
        } else if (!getServerLevel(this.mob).getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        } else {
            return !this.isOpen();
        }
    }

    @Override
    public void start() {
        super.start();
        this.breakProgress = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.breakProgress <= this.getMaxProgress() && !this.isOpen() && this.doorPos.closerToCenterThan(this.mob.position(), 2.0);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.level().destroyBlockProgress(this.mob.getId(), this.doorPos, -1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.mob.getRandom().nextInt(20) == 0) {
            this.mob.level().levelEvent(1019, this.doorPos, 0);
            if (!this.mob.swinging) {
                this.mob.swing(this.mob.getUsedItemHand());
            }
        }

        this.breakProgress += 90;
        int i = (int)((float)this.breakProgress / (float)this.getMaxProgress() * 10.0F);
        if (i != this.prevBreakProgress) {
            this.mob.level().destroyBlockProgress(this.mob.getId(), this.doorPos, i);
            this.prevBreakProgress = i;
        }

        if (this.breakProgress >= this.getMaxProgress()) {
            this.mob.level().removeBlock(this.doorPos, false);
            this.mob.level().levelEvent(1021, this.doorPos, 0);
            this.mob.level().levelEvent(2001, this.doorPos, Block.getId(this.mob.level().getBlockState(this.doorPos)));
        }
    }
}
