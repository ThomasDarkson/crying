package crying.tools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.tools.Crying;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.GameEvent.Emitter;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "onBreak", at = @At("HEAD"), cancellable = true)
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> info) {
        ItemStack stack = player.getMainHandStack().copy();
        ItemStack stack2 = player.getMainHandStack().copy();
        if (stack.getItem() == Crying.THE_CRYING_BEING && !player.isCreative() && state.getBlock() != Blocks.BEDROCK) {
            BlockState blockState = world.getBlockState(pos);
            if (!blockState.isAir()) {
                FluidState fluidState = world.getFluidState(pos);
                if (!(blockState.getBlock() instanceof AbstractFireBlock)) {
                    world.syncWorldEvent(2001, pos, Block.getRawIdFromState(blockState));
                }

                stack.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
                stack2.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
                stack.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), 3);
                stack2.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), 1);

                BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(pos) : null;
                Block.dropStacks(blockState, world, pos, blockEntity, player, (player.isSneaking() ? stack2 : stack));
        
                boolean bl = world.setBlockState(pos, fluidState.getBlockState(), 3, 65536);
                if (bl) {
                    world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, Emitter.of(player, blockState));
                }
            }
        }
    }
}
