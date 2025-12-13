package crying.items;

import crying.Crying;
import crying.effects.BaneOfCriers;
import crying.interfaces.FoodVars;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.level.ItemLike;

public class CryingFoodItem extends Item {
    public static final Consumable.Builder DEFAULT_FOOD_COMPONENT = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SLOWNESS, 540, 2), 0.55f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(BaneOfCriers.BANE_OF_CRIERS, 100, 0), 1f));

    public CryingFoodItem(String id, FoodProperties component, Consumable consumableComponent, ItemLike itemAfter, float cooldown, boolean enchanted) {
        super(new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Crying.ID, id)))
            .fireResistant()
            .useCooldown(cooldown)
            .rarity(enchanted ? Rarity.EPIC : Rarity.COMMON)
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, enchanted)
            .stacksTo(enchanted ? 1 : 32)
            .food(component, consumableComponent == null ? DEFAULT_FOOD_COMPONENT.build() : consumableComponent));

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register((itemGroup) -> itemGroup.addAfter(itemAfter, this));
    }

    public int restoresSanity() {
        return 0;
    }

    public static float getExtraProtectionFormula(Player player) {
        FoodVars food = (FoodVars) (Object) player;
        return ((float) food.getEatenCryingFoodCount() / Crying.MAX_CRYING_FOOD_COUNT) * 0.8F;
    }
}
