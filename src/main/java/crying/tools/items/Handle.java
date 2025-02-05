package crying.tools.items;

import crying.tools.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class Handle {
    public Item item = null;

    public Handle() {
        create();
        Crying.register(item, "handle");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> itemGroup.addAfter(Items.STICK, item));
    }

    public void create() {
        this.item = new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.MOD_ID, "handle"))));
    }
}
