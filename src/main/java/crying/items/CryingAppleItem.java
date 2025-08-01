package crying.items;

import crying.Crying;
import crying.effects.BaneOfCriers;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class CryingAppleItem extends Item {
    public CryingAppleItem() {
        super(new Item.Settings()
        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_apple")))
        .useCooldown(60f)
        .food(new FoodComponent(20, 20F, true), ConsumableComponents.food()
        .consumeSeconds(6.2F)
        .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 540, 2), 0.55f))
        .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(BaneOfCriers.BANE_OF_CRIERS, 100, 0), 1f))
        .build()));
        
        Crying.register(this, "crying_apple");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register((itemGroup) -> itemGroup.addAfter(Items.ENCHANTED_GOLDEN_APPLE, this));
    }
}
