package crying.entities;

import crying.Crying;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CriersHeartBlockEntity extends BlockEntity implements BlockEntityTicker<CriersHeartBlockEntity> {
    public float heartbeatPhase = 0.0f;
    public float SPEED = 5.5F;
    public float speedModifier = 0;

    public CriersHeartBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(Crying.CRIERS_HEART_BLOCK_ENTITY, pos, state);
    }

    public CriersHeartBlockEntity(BlockPos pos, BlockState state) {
        super(Crying.CRIERS_HEART_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CriersHeartBlockEntity blockEntity) {
    }

    public static void ticker(World world, BlockPos pos, BlockState state, CriersHeartBlockEntity blockEntity) {
        if (world.isThundering()) 
            blockEntity.speedModifier = 2.5F;
        else if (world.isRaining())
            blockEntity.speedModifier = -1.5F;
        else
            blockEntity.speedModifier = 0;

        blockEntity.heartbeatPhase += (blockEntity.SPEED + blockEntity.speedModifier) / 100f;
        if (blockEntity.heartbeatPhase > 1.0f)
            blockEntity.heartbeatPhase = 0f;
    }
}