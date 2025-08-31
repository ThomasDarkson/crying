package crying.other;

import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

public class CrierExplosionBehavior extends ExplosionBehavior {
    boolean shouldDoDamage = false;
    public CrierExplosionBehavior(boolean shouldDoDamage) {
        super();
        this.shouldDoDamage = shouldDoDamage;
    }

    @Override
    public boolean shouldDamage(Explosion explosion, Entity entity) {
        return shouldDoDamage;
    }

    @Override
    public Optional<Float> getBlastResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState) {
        return blockState.isAir() && fluidState.isEmpty() ? Optional.empty() : (blockState.getHardness(world, pos) < 0 ? Optional.of(blockState.getBlock().getBlastResistance()) : Optional.of(0.01F));
    }
}
