package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.Crying;
import crying.interfaces.CryingTool;
import crying.tools.sword.AbstractCryingSwordItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    int breakCount = 0;
    @Inject(method = "calcBlockBreakingDelta", at = @At("HEAD"), cancellable = true)
    protected void calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> info) {
        if (Crying.isTheCryingBeing(player.getMainHandStack()) && state.getBlock() != Blocks.BEDROCK)
            info.setReturnValue((float) Integer.MAX_VALUE);
    }

    @Inject(method = "onUseWithItem", at = @At("HEAD"), cancellable = true)
    protected void onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
        if (stack.getItem() instanceof CryingTool tool && !(tool instanceof AbstractCryingSwordItem) && tool.getCoreIngredient() == Items.IRON_INGOT) {
            if (world.getRandom().nextFloat() < 0.33F) {
                player.swingHand(hand);
                ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.IRON_INGOT));
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
                stack.damage(1, player);
            }
            else {
                breakCount++;
                AbstractBlock block = (AbstractBlock) (Object) this;
                if (breakCount > (block == Blocks.IRON_BLOCK ? 11 : 2)) {
                    world.breakBlock(pos, false, player);
                }
            }
        }
    }
}
