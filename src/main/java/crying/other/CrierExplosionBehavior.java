package crying.other;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class CrierExplosionBehavior extends ExplosionDamageCalculator {
    boolean shouldDoDamage = false;
    public CrierExplosionBehavior(boolean shouldDoDamage) {
        super();
        this.shouldDoDamage = shouldDoDamage;
    }

    @Override
    public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        return shouldDoDamage;
    }

    @Override
    public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter world, BlockPos pos, BlockState blockState, FluidState fluidState) {
        return blockState.isAir() && fluidState.isEmpty() ? Optional.empty() : (blockState.getDestroySpeed(world, pos) < 0 ? Optional.of(blockState.getBlock().getExplosionResistance()) : Optional.of(0.01F));
    }
}
