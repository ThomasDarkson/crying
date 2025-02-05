package crying.tools.items;

import crying.tools.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class CryingApple extends Item {
    public CryingApple() {
        super(new Item.Settings()
        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.MOD_ID, "crying_apple")))
        .food(new FoodComponent(4, 2.4F, true), ConsumableComponents.food().consumeSeconds(1.5F).build()));
        
        Crying.register(this, "crying_apple");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register((itemGroup) -> itemGroup.addAfter(Items.ENCHANTED_GOLDEN_APPLE, this));
    }
}
