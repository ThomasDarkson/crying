package crying.blocks.core;

import crying.Crying;
import crying.interfaces.HasUniqueItemSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CoreBlock extends Block implements HasUniqueItemSettings {
    public static final VoxelShape SHAPE = Block.column(8.0, 0.0, 8.0);

    public CoreBlock(Properties settings, String id) {
        super(settings);

        Crying.registerBlock(this, id);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public boolean isFireProof() {
        return true;
    }

    @Override
    public int getMaxCount() {
        return 1;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }
}
