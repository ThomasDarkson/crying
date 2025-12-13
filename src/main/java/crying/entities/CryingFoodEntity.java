package crying.entities;

import crying.Crying;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CryingFoodEntity extends BlockEntity implements BlockEntityTicker<CryingFoodEntity> {
    public CryingFoodEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public CryingFoodEntity(BlockPos pos, BlockState state) {
        this(Crying.CRYING_FOOD_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state, CryingFoodEntity blockEntity) {
    }
    
    public static void ticker(Level world, BlockPos pos, BlockState state, CryingFoodEntity blockEntity) {
    }
}
