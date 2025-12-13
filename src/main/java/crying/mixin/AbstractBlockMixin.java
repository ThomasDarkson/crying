package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.Crying;
import crying.enums.ToolType;
import crying.interfaces.CryingTool;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(BlockBehaviour.class)
public class AbstractBlockMixin {
    int breakCount = 0;
    @Inject(method = "getDestroyProgress", at = @At("HEAD"), cancellable = true)
    protected void calcBlockBreakingDelta(BlockState state, Player player, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Float> info) {
        if (Crying.isTheCryingBeing(player.getMainHandItem()) && state.getDestroySpeed(world, pos) > 0)
            info.setReturnValue((float) Integer.MAX_VALUE);
    }

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    protected void onUseWithItem(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> info) {
        if (stack.getItem() instanceof CryingTool tool && tool.getToolType() != ToolType.SWORD && tool.getCoreIngredient() == Items.IRON_INGOT) {
            if (world.getRandom().nextFloat() < 0.33F) {
                player.swing(hand);
                ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.IRON_INGOT));
                itemEntity.setDefaultPickUpDelay();
                world.addFreshEntity(itemEntity);
                stack.hurtWithoutBreaking(1, player);
            }
            else {
                breakCount++;
                BlockBehaviour block = (BlockBehaviour) (Object) this;
                if (breakCount > (block == Blocks.IRON_BLOCK ? 11 : 2)) {
                    world.destroyBlock(pos, false, player);
                }
            }
        }
    }
}
