package crying.items;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class AbstractCryingIngotItem extends Item {
    public AbstractCryingIngotItem(String id) {
        super(new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, id)))
            .fireproof());

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_INGOT, this));
    }
}
