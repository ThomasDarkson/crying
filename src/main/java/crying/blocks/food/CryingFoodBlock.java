package crying.blocks.food;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import crying.Crying;
import crying.entities.CryingFoodEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CryingFoodBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Shapes.or(Shapes.box(0.25, 0, 0.25, 0.75, 0.125, 0.75));

    public CryingFoodBlock(Properties settings) {
        super(settings);
    }

    public CryingFoodBlock(Properties settings, String id) {
        this(settings);
        
        Crying.registerBlock(this, id);
    }

    @Override
    protected void spawnDestroyParticles(Level world, Player player, BlockPos pos, BlockState state) {
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CryingFoodEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CryingFoodBlock::new);
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (blockEntity != null && blockEntity instanceof CryingFoodEntity) {
            if (this.stack() != null) {
                ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), this.stack());
                item.setUnlimitedLifetime();
                world.addFreshEntity(item);
            }
        }

        super.playerDestroy(world, player, pos, state, blockEntity, tool);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, Crying.CRYING_FOOD_BLOCK_ENTITY, CryingFoodEntity::ticker);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public ItemStack stack() {
        return null;
    }
}
