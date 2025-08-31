package crying.blocks;

import com.mojang.serialization.MapCodec;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LostCryingBlock extends FacingBlock {
    private static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(Block.createCuboidShape(4, 16, 4, 12, 24, 12), Block.createCuboidShape(0, 0, 0, 16, 16, 16), BooleanBiFunction.OR);

    public LostCryingBlock() {
        this(Settings.create().
            registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Crying.ID, "lost_crying_block"))).
            strength(6.4375F, (float) Integer.MAX_VALUE));
    }
    
    protected LostCryingBlock(Settings settings) {
        super(settings);
        this.setDefaultState(((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));

        Crying.registerBlock(this, "lost_crying_block");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> itemGroup.add(this.asItem()));
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ActionResult result = ActionResult.PASS;
        Random random = world.getRandom();
        if (stack.getItem() == Items.GOLD_INGOT) {
            if (random.nextFloat() <= 0.1F) {
                if (world instanceof ServerWorld serverWorld) {
                    Crying.LOST_CRIER.spawn(serverWorld, pos, SpawnReason.TRIGGERED);
                    result = ActionResult.SUCCESS;
                }
                else
                    result = ActionResult.FAIL;
            }
            else
                result = ActionResult.FAIL;

            stack.decrementUnlessCreative(1, player);
            world.breakBlock(pos, false);
        }
        player.swingHand(hand, player instanceof ServerPlayerEntity);
        return result;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    protected MapCodec<? extends LostCryingBlock> getCodec() {
        return createCodec(LostCryingBlock::new);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
