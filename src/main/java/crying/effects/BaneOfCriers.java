package crying.effects;

import crying.Crying;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BaneOfCriers extends MobEffect {
    public static Holder<MobEffect> BANE_OF_CRIERS = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Crying.ID, "bane_of_criers"), new BaneOfCriers());

	public static void initialize() {

	}
	
    protected BaneOfCriers() {
		super(MobEffectCategory.HARMFUL, 5312458, ParticleTypes.FALLING_OBSIDIAN_TEAR);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}
}
