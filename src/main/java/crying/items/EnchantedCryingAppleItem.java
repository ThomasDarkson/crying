package crying.items;

import crying.Crying;
import crying.effects.BaneOfCriers;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;

public class EnchantedCryingAppleItem extends CryingFoodItem {
    public static final ConsumableComponent.Builder FOOD_COMPONENT = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 540, 3), 0.88f))
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 12360, 3), 1F))
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 12360, 3), 1F))
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(BaneOfCriers.BANE_OF_CRIERS, 600, 0), 0.95f));

    public EnchantedCryingAppleItem() {
        super("enchanted_crying_apple", new FoodComponent(20, 20F, false), FOOD_COMPONENT.build(), Crying.CRYING_APPLE, 61.8F, true);
    }

    @Override
    public int restoresSanity() {
        return 10;
    }
}
