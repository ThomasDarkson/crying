package crying.tools.effects;

import crying.tools.Crying;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class LoveOfTheFeline extends StatusEffect {
    public static RegistryEntry<StatusEffect> LOVE_OF_THE_FELINE = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Crying.ID, "love_of_the_feline"), new LoveOfTheFeline());
    
    public static void initialize() {
	}
	
    protected LoveOfTheFeline() {
		super(StatusEffectCategory.BENEFICIAL, 16753920);

        addAttributeModifier(EntityAttributes.ATTACK_DAMAGE, Identifier.of(Crying.ID, "effect.strength_cat"), 6.18d, Operation.ADD_VALUE);
		addAttributeModifier(EntityAttributes.JUMP_STRENGTH, Identifier.of(Crying.ID, "effect.jump_boost_cat"), 0.42d, Operation.ADD_VALUE);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}
