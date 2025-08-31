package crying.items;

import crying.effects.BaneOfCriers;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;

public class CryingCarrotItem extends CryingFoodItem {
    public static final ConsumableComponent.Builder FOOD_COMPONENT = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 540, 3), 0.88f))
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(BaneOfCriers.BANE_OF_CRIERS, 600, 0), 0.95f));

    public CryingCarrotItem() {
        super("crying_carrot", new FoodComponent(6, 18, true), FOOD_COMPONENT.build(), Items.GOLDEN_CARROT, 6.18F, false);
    }

    @Override
    public int restoresSanity() {
        return 2;
    }
}
