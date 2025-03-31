package crying.tools.goals;

import java.util.List;
import net.minecraft.util.math.random.Random;

import org.jetbrains.annotations.Nullable;

import crying.tools.entities.CryingCatEntity;
import crying.tools.other.CryingLootTable;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.item.ItemStack;

public class SleepAndGiveGiftsToPlayerGoal extends Goal {
    private CryingCatEntity cat = null;
    @Nullable
    private PlayerEntity owner;
    @Nullable
    private BlockPos bedPos;
    private int ticksOnBed;

    public SleepAndGiveGiftsToPlayerGoal(CryingCatEntity cat) {
        this.cat = cat;
    }

    @Override
    public boolean canStart() {
        if (!this.cat.isTamed()) {
            return false;
        }
        if (this.cat.isSitting()) {
            return false;
        }
        LivingEntity lv = this.cat.getOwner();
        if (lv instanceof PlayerEntity) {
            this.owner = (PlayerEntity) lv;
            if (!lv.isSleeping()) {
                return false;
            }
            if (this.cat.squaredDistanceTo(this.owner) > 100.0) {
                return false;
            }
            BlockPos lv2 = this.owner.getBlockPos();
            BlockState lv3 = ((World) this.cat.getWorld()).getBlockState(lv2);
            if (lv3.isIn(BlockTags.BEDS)) {
                this.bedPos = lv3.getOrEmpty(BedBlock.FACING).map(direction -> lv2.offset(direction.getOpposite())).orElseGet(() -> new BlockPos(lv2));
                return !this.cannotSleep();
            }
        }
        return false;
    }

    private boolean cannotSleep() {
        List < CryingCatEntity > list = this.cat.getWorld().getNonSpectatingEntities(CryingCatEntity.class, new Box(this.bedPos).expand(2.0));
        for (CryingCatEntity lv: list) {
            if (lv == this.cat || !lv.isInSleepingPose() && !lv.getHeadDown()) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        return this.cat.isTamed() && !this.cat.isSitting() && this.owner != null && this.owner.isSleeping() && this.bedPos != null && !this.cannotSleep();
    }

    @Override
    public void start() {
        if (this.bedPos != null) {
            this.cat.setInSittingPose(false);
            this.cat.getNavigation().startMovingTo(this.bedPos.getX(), this.bedPos.getY(), this.bedPos.getZ(), 1.1f);
        }
    }

    @Override
    public void stop() {
        this.cat.setInSleepingPose(false);
        if (this.owner.getSleepTimer() >= 100) {
            this.dropMorningGifts();
        }
        this.ticksOnBed = 0;
        this.cat.setHeadDownPublic(false);
        this.cat.getNavigation().stop();
    }

    private void dropMorningGifts() {
        Random lv = this.cat.getRandom();
        BlockPos.Mutable lv2 = new BlockPos.Mutable();
        lv2.set(this.cat.isLeashed() ? this.cat.getLeashHolder().getBlockPos() : this.cat.getBlockPos());
        this.cat.teleport(lv2.getX() + lv.nextInt(3), lv2.getY() + lv.nextInt(3), lv2.getZ() + lv.nextInt(3), false);
        lv2.set(this.cat.getBlockPos());
        LootTable lv3 = ((World) this.cat.getWorld()).getServer().getReloadableRegistries().getLootTable(CryingLootTable.CRYING_CAT_MORNING_GIFT);
        LootWorldContext lv4 = new LootWorldContext.Builder((ServerWorld) this.cat.getWorld()).add(LootContextParameters.ORIGIN, this.cat.getPos()).add(LootContextParameters.THIS_ENTITY, this.cat).build(LootContextTypes.GIFT);
        ObjectArrayList < ItemStack > list = lv3.generateLoot(lv4);
        for (ItemStack lv5: list) {
            this.cat.getWorld().spawnEntity(new ItemEntity((World) this.cat.getWorld(), (double) lv2.getX() - (double) MathHelper.sin(this.cat.bodyYaw * ((float) Math.PI / 180)), lv2.getY(), (double) lv2.getZ() + (double) MathHelper.cos(this.cat.bodyYaw * ((float) Math.PI / 180)), lv5));
        }
    }

    @Override
    public void tick() {
        if (this.owner != null && this.bedPos != null) {
            this.cat.setInSittingPose(false);
            this.cat.getNavigation().startMovingTo(this.bedPos.getX(), this.bedPos.getY(), this.bedPos.getZ(), 1.1f);
            if (this.cat.squaredDistanceTo(this.owner) < 2.5) {
                ++this.ticksOnBed;
                if (this.ticksOnBed > this.getTickCount(16)) {
                    this.cat.setInSleepingPose(true);
                    this.cat.setHeadDownPublic(false);
                } else {
                    this.cat.lookAtEntity(this.owner, 45.0f, 45.0f);
                    this.cat.setHeadDownPublic(true);
                }
            } else {
                this.cat.setInSleepingPose(false);
            }
        }
    }
}