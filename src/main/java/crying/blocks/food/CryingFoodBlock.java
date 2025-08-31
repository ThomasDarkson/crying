package crying.blocks.food;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import crying.Crying;
import crying.entities.CryingFoodEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CryingFoodBlock extends BlockWithEntity {
    private static final VoxelShape SHAPE = VoxelShapes.union(VoxelShapes.cuboid(0.25, 0, 0.25, 0.75, 0.125, 0.75));

    public CryingFoodBlock(Settings settings) {
        super(settings);
    }

    public CryingFoodBlock(Settings settings, String id) {
        this(settings);
        
        Crying.registerBlock(this, id);
    }

    @Override
    protected void spawnBreakParticles(World world, PlayerEntity player, BlockPos pos, BlockState state) {
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CryingFoodEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(CryingFoodBlock::new);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (blockEntity != null && blockEntity instanceof CryingFoodEntity) {
            if (this.stack() != null) {
                ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), this.stack());
                item.setNeverDespawn();
                world.spawnEntity(item);
            }
        }

        super.afterBreak(world, player, pos, state, blockEntity, tool);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, Crying.CRYING_FOOD_BLOCK_ENTITY, CryingFoodEntity::ticker);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public ItemStack stack() {
        return null;
    }
}
