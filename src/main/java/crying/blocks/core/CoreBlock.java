package crying.blocks.core;

import crying.Crying;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class CoreBlock extends Block {
    public static final VoxelShape SHAPE = Block.createColumnShape(8.0, 0.0, 8.0);

    public CoreBlock(Settings settings, String id) {
        super(settings);

        Crying.registerBlock(this, id);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}
