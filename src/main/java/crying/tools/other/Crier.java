package crying.tools.other;

import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import crying.tools.Crying;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class Crier extends StatusEffect {
    private static boolean initialized = false;
    public static RegistryEntry<StatusEffect> CRIER = null;

    public static void setupCrier()
    {
        if (initialized)
            return;

        CRIER = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Crying.MOD_ID, "crier"), new Crier());
    }

    private int resistanceAmplifier = 0;
    protected Crier() {
		super(StatusEffectCategory.BENEFICIAL, 2818424);
        addAttributeModifier(EntityAttributes.ATTACK_DAMAGE, Identifier.of(Crying.MOD_ID, "effect.strength"), 3.0d, Operation.ADD_VALUE);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

    public int getAmplifier()
    {
        return resistanceAmplifier;
    }

    public static void setAmplifier(Crier crier, int amplifier)
    {
        crier.resistanceAmplifier = amplifier;
    }
}
