package crying.blocks;

import com.mojang.serialization.MapCodec;

import crying.Crying;
import crying.entities.CriersHeartBlockEntity;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroups;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CriersHeartBlock extends BlockWithEntity {
    static final VoxelShape SHAPE = VoxelShapes.union(
		VoxelShapes.cuboid(0.4375, 0, 0.4375, 0.5, 0.0625, 0.5),
		VoxelShapes.cuboid(0.375, 0.0625, 0.4375, 0.5625, 0.125, 0.5),
		VoxelShapes.cuboid(0.3125, 0.125, 0.4375, 0.625, 0.1875, 0.5),
		VoxelShapes.cuboid(0.25, 0.1875, 0.4375, 0.6875, 0.25, 0.5),
		VoxelShapes.cuboid(0.1875, 0.25, 0.4375, 0.75, 0.3125, 0.5),
		VoxelShapes.cuboid(0.5, 0.3125, 0.4375, 0.75, 0.375, 0.5),
		VoxelShapes.cuboid(0.1875, 0.3125, 0.4375, 0.4375, 0.375, 0.5),
		VoxelShapes.cuboid(0.4375, 0.3125, 0.4375, 0.5, 0.375, 0.5),
		VoxelShapes.cuboid(0.25, 0.375, 0.4375, 0.4375, 0.4375, 0.5),
		VoxelShapes.cuboid(0.5, 0.375, 0.4375, 0.6875, 0.4375, 0.5),
		VoxelShapes.cuboid(0.5625, 0.4375, 0.4375, 0.625, 0.5, 0.5),
		VoxelShapes.cuboid(0.3125, 0.4375, 0.4375, 0.375, 0.5, 0.5),
		VoxelShapes.cuboid(0.40625, 0.375, 0.44375, 0.45625, 0.5625, 0.49375),
		VoxelShapes.cuboid(0.40625, 0.4375, 0.4375, 0.45625, 0.625, 0.4875),
		VoxelShapes.cuboid(0.5, 0.4375, 0.4375, 0.5625, 0.625, 0.5),
		VoxelShapes.cuboid(0.3125, 0.125, 0.375, 0.625, 0.375, 0.4375),
		VoxelShapes.cuboid(0.3125, 0.125, 0.5, 0.625, 0.375, 0.5625)
	);

    public CriersHeartBlock(Settings settings) {
        super(settings);

        Crying.registerBlock(this, "criers_heart");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((itemGroup) -> itemGroup.add(this.asItem()));
    }
    
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, Crying.CRIERS_HEART_BLOCK_ENTITY, CriersHeartBlockEntity::ticker);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos arg0, BlockState arg1) {
        return new CriersHeartBlockEntity(arg0, arg1);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(CriersHeartBlock::new);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
