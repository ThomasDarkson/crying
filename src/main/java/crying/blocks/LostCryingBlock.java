package crying.blocks;

import com.mojang.serialization.MapCodec;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LostCryingBlock extends DirectionalBlock {
    private static final VoxelShape SHAPE = Shapes.join(Block.box(4, 16, 4, 12, 24, 12), Block.box(0, 0, 0, 16, 16, 16), BooleanOp.OR);

    public LostCryingBlock() {
        this(Properties.of().
            setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Crying.ID, "lost_crying_block"))).
            strength(6.4375F, (float) Integer.MAX_VALUE));
    }
    
    protected LostCryingBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH));

        Crying.registerBlock(this, "lost_crying_block");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register((itemGroup) -> itemGroup.accept(this.asItem()));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = InteractionResult.PASS;
        RandomSource random = world.getRandom();
        if (stack.getItem() == Items.GOLD_INGOT) {
            if (random.nextFloat() <= 0.1F) {
                if (world instanceof ServerLevel serverWorld) {
                    Crying.LOST_CRIER.spawn(serverWorld, pos, EntitySpawnReason.TRIGGERED);
                    result = InteractionResult.SUCCESS;
                }
                else
                    result = InteractionResult.FAIL;
            }
            else
                result = InteractionResult.FAIL;

            stack.consume(1, player);
            world.destroyBlock(pos, false);
        }
        player.swing(hand, player instanceof ServerPlayer);
        return result;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected MapCodec<? extends LostCryingBlock> codec() {
        return simpleCodec(LostCryingBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
