package crying.tools.items;

import crying.tools.Crying;
import crying.tools.effects.BaneOfCriers;
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

public class CryingApple extends Item {
    public CryingApple() {
        super(new Item.Settings()
        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_apple")))
        .food(new FoodComponent(20, 20F, true), ConsumableComponents.food()
        .consumeSeconds(6.2F)
        .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 600, 3), 0.75f))
        .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(BaneOfCriers.EFFECT, 100, 0), 1f))
        .build()));
        
        Crying.register(this, "crying_apple");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register((itemGroup) -> itemGroup.addAfter(Items.ENCHANTED_GOLDEN_APPLE, this));
    }
}
