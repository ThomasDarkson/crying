package crying.entities;

import crying.Crying;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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
    public void tick(Level world, BlockPos pos, BlockState state, CriersHeartBlockEntity blockEntity) {
    }

    public static void ticker(Level world, BlockPos pos, BlockState state, CriersHeartBlockEntity blockEntity) {
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