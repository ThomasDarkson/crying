package crying.mixin;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import crying.interfaces.SanityInterface;
import crying.interfaces.SanityManager;
import crying.tools.hoe.AbstractCryingHoeItem;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.world.World;

@Mixin(FoodComponent.class)
public abstract class FoodComponentMixin {
    @Inject(method = "onConsume", at = @At("TAIL"))
    public void onConsume(World world, LivingEntity user, ItemStack stack, ConsumableComponent consumable, CallbackInfo info) {
        if (user instanceof PlayerEntity player) {
            Item item = stack.getItem();
            FoodComponent food = (FoodComponent) (Object) this;
            SanityManager manager = ((SanityInterface) (Object) player).getManagerOverride_crying();
            if (item == Crying.CRYING_APPLE) {
                manager.decreaseLevel(-2F);
                manager.increasePermanentMaxLevel();
            }
            else if (item == Items.PUFFERFISH || item == Items.ROTTEN_FLESH) {
                manager.setPreventRegenTicks(item == Items.PUFFERFISH ? Crying.tickSecond(1800) : Crying.tickSecond(300), player);
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
                        if (effect instanceof ApplyEffectsConsumeEffect apply) {
                            Iterator<StatusEffectInstance> i = apply.effects().iterator();
                            while (i.hasNext()) {
                                StatusEffectInstance instance = i.next();
                                if (instance.getEffectType().value().getCategory() == StatusEffectCategory.HARMFUL) {
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
                return itemStack.getItem() instanceof AbstractCryingHoeItem tool && tool.getCoreIngredient() == Items.DIAMOND;
            })) {
                player.getHungerManager().add(food.nutrition(), food.saturation());
            }
        }
    }   
}
