package crying.mixin;

import java.util.Iterator;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import crying.enums.CollapsingReason;
import crying.interfaces.CryingTool;
import crying.interfaces.FoodVars;
import crying.interfaces.SanityManager;
import crying.items.CryingFoodItem;

@Mixin(FoodProperties.class)
public abstract class FoodComponentMixin {
    @Inject(method = "onConsume", at = @At("TAIL"))
    public void onConsume(Level world, LivingEntity user, ItemStack stack, Consumable consumable, CallbackInfo info) {
        if (user instanceof Player player) {
            Item item = stack.getItem();
            FoodProperties food = (FoodProperties) (Object) this;
            SanityManager manager = Crying.getSanityManager(player);
            if (item instanceof CryingFoodItem foodItem) {
                manager.decreaseLevel(-foodItem.restoresSanity());
                FoodVars foodVar = ((FoodVars) (Object) player);
                foodVar.setEatenCryingFoodCount(foodVar.getEatenCryingFoodCount() + 1);
            }
            else if (item == Items.PUFFERFISH || item == Items.ROTTEN_FLESH) {
                manager.collapse(item == Items.PUFFERFISH ? Crying.tickSecond(1800) : Crying.tickSecond(300), player, CollapsingReason.BAD_FOOD);
            }
            else if (item == Items.HONEY_BOTTLE) {
                manager.decreaseLevel(-20F);
            }
            else {
                if (!food.canAlwaysEat()) {
                    boolean badFood = false;
                    Iterator<ConsumeEffect> it = consumable.onConsumeEffects().iterator();
                    while (it.hasNext()) {
                        if (badFood)
                            break;

                        ConsumeEffect effect = it.next();
                        if (effect instanceof ApplyStatusEffectsConsumeEffect apply) {
                            Iterator<MobEffectInstance> i = apply.effects().iterator();
                            while (i.hasNext()) {
                                MobEffectInstance instance = i.next();
                                if (instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                                    badFood = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (!badFood) {
                        manager.decreaseLevel(-Math.abs((float) food.nutrition() / 2F));
                    }
                    else {
                        manager.decreaseLevel(Math.abs((float) food.nutrition() / 2F));
                    }
                }
            }

            if (player.getInventory().contains((itemStack) -> {
                return itemStack.getItem() instanceof CryingTool tool && tool.getCoreIngredient() == Items.DIAMOND;
            })) {
                player.getFoodData().eat(food.nutrition(), food.saturation());
            }
        }
    }   
}
