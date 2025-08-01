package crying.goals;

import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.goal.DoorInteractGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

public class FastBreakDoorGoal extends DoorInteractGoal {
    protected int breakProgress;
    protected int prevBreakProgress;
    protected int maxProgress;

    public FastBreakDoorGoal(MobEntity mob, Predicate<Difficulty> difficultySufficientPredicate) {
        super(mob);
        this.prevBreakProgress = -1;
        this.maxProgress = -1;
    }
    
    protected int getMaxProgress() {
        return Math.max(240, this.maxProgress);
    }

    @Override
    public boolean canStart() {
        if (!super.canStart()) {
            return false;
        } else if (!getServerWorld(this.mob).getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            return false;
        } else {
            return !this.isDoorOpen();
        }
    }

    @Override
    public void start() {
        super.start();
        this.breakProgress = 0;
    }

    @Override
    public boolean shouldContinue() {
        return this.breakProgress <= this.getMaxProgress() && !this.isDoorOpen() && this.doorPos.isWithinDistance(this.mob.getPos(), 2.0);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.getWorld().setBlockBreakingInfo(this.mob.getId(), this.doorPos, -1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.mob.getRandom().nextInt(20) == 0) {
            this.mob.getWorld().syncWorldEvent(1019, this.doorPos, 0);
            if (!this.mob.handSwinging) {
                this.mob.swingHand(this.mob.getActiveHand());
            }
        }

        this.breakProgress += 90;
        int i = (int)((float)this.breakProgress / (float)this.getMaxProgress() * 10.0F);
        if (i != this.prevBreakProgress) {
            this.mob.getWorld().setBlockBreakingInfo(this.mob.getId(), this.doorPos, i);
            this.prevBreakProgress = i;
        }

        if (this.breakProgress >= this.getMaxProgress()) {
            this.mob.getWorld().removeBlock(this.doorPos, false);
            this.mob.getWorld().syncWorldEvent(1021, this.doorPos, 0);
            this.mob.getWorld().syncWorldEvent(2001, this.doorPos, Block.getRawIdFromState(this.mob.getWorld().getBlockState(this.doorPos)));
        }
    }
}
