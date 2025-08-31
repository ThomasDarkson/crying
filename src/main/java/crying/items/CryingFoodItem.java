package crying.items;

import crying.Crying;
import crying.effects.BaneOfCriers;
import crying.interfaces.FoodVars;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class CryingFoodItem extends Item {
    public static final ConsumableComponent.Builder DEFAULT_FOOD_COMPONENT = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 540, 2), 0.55f))
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(BaneOfCriers.BANE_OF_CRIERS, 100, 0), 1f));

    public CryingFoodItem(String id, FoodComponent component, ConsumableComponent consumableComponent, ItemConvertible itemAfter, float cooldown, boolean enchanted) {
        super(new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, id)))
            .fireproof()
            .useCooldown(cooldown)
            .rarity(enchanted ? Rarity.EPIC : Rarity.COMMON)
            .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, enchanted)
            .maxCount(enchanted ? 1 : 32)
            .food(component, consumableComponent == null ? DEFAULT_FOOD_COMPONENT.build() : consumableComponent));

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register((itemGroup) -> itemGroup.addAfter(itemAfter, this));
    }

    public int restoresSanity() {
        return 0;
    }

    public static float getExtraProtectionFormula(PlayerEntity player) {
        FoodVars food = (FoodVars) (Object) player;
        return ((float) food.getEatenCryingFoodCount() / Crying.MAX_CRYING_FOOD_COUNT) * 0.8F;
    }
}
