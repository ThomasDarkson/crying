package crying.entities;

import crying.Crying;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CryingFoodEntity extends BlockEntity implements BlockEntityTicker<CryingFoodEntity> {
    public CryingFoodEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public CryingFoodEntity(BlockPos pos, BlockState state) {
        this(Crying.CRYING_FOOD_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CryingFoodEntity blockEntity) {
    }
    
    public static void ticker(World world, BlockPos pos, BlockState state, CryingFoodEntity blockEntity) {
    }
}
