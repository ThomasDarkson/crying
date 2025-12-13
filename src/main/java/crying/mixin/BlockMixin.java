package crying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.Crying;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.level.material.FluidState;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "playerWillDestroy", at = @At("HEAD"), cancellable = true)
    public void onBreak(Level world, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> info) {
        ItemStack stack = player.getMainHandItem().copy();
        ItemStack stack2 = player.getMainHandItem().copy();
        if (Crying.isTheCryingBeing(stack) && !player.isCreative() && state.getBlock() != Blocks.BEDROCK) {
            BlockState blockState = world.getBlockState(pos);
            if (!blockState.isAir()) {
                FluidState fluidState = world.getFluidState(pos);
                if (!(blockState.getBlock() instanceof BaseFireBlock)) {
                    world.levelEvent(2001, pos, Block.getId(blockState));
                }

                stack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                stack2.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                stack.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), 3);
                stack2.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), 1);

                BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(pos) : null;
                Block.dropResources(blockState, world, pos, blockEntity, player, (player.isShiftKeyDown() ? stack2 : stack));
        
                boolean bl = world.setBlock(pos, fluidState.createLegacyBlock(), 3, 65536);
                if (bl) {
                    world.gameEvent(GameEvent.BLOCK_DESTROY, pos, Context.of(player, blockState));
                }
            }
        }
    }
}
