package crying.items;

import crying.Crying;
import crying.effects.BaneOfCriers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class EnchantedCryingAppleItem extends CryingFoodItem {
    public static final Consumable.Builder FOOD_COMPONENT = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SLOWNESS, 540, 3), 0.88f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH, 12360, 3), 1F))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.RESISTANCE, 12360, 3), 1F))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(BaneOfCriers.BANE_OF_CRIERS, 600, 0), 0.95f));

    public EnchantedCryingAppleItem() {
        super("enchanted_crying_apple", new FoodProperties(20, 20F, false), FOOD_COMPONENT.build(), Crying.CRYING_APPLE, 61.8F, true);
    }

    @Override
    public int restoresSanity() {
        return 10;
    }
}
