package crying.items;

import crying.effects.BaneOfCriers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class CryingCarrotItem extends CryingFoodItem {
    public static final Consumable.Builder FOOD_COMPONENT = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SLOWNESS, 540, 3), 0.88f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(BaneOfCriers.BANE_OF_CRIERS, 600, 0), 0.95f));

    public CryingCarrotItem() {
        super("crying_carrot", new FoodProperties(6, 18, true), FOOD_COMPONENT.build(), Items.GOLDEN_CARROT, 6.18F, false);
    }

    @Override
    public int restoresSanity() {
        return 2;
    }
}
