package crying.tools.effects;

import crying.tools.Crying;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class BaneOfCriers extends StatusEffect {
    public static RegistryEntry<StatusEffect> EFFECT = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Crying.MOD_ID, "bane_of_criers_effect"), new BaneOfCriers());

	public static void initialize() {

	}
	
    protected BaneOfCriers() {
		super(StatusEffectCategory.HARMFUL, 2818424, ParticleTypes.FALLING_OBSIDIAN_TEAR);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}
