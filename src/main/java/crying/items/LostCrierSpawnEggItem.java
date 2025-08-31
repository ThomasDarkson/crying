package crying.items;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class LostCrierSpawnEggItem extends SpawnEggItem {
    public LostCrierSpawnEggItem() {
        super(Crying.LOST_CRIER, new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "lost_crier_spawn_egg"))));

        Crying.register(this, "lost_crier_spawn_egg");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register((itemGroup) -> itemGroup.addAfter(Crying.CRIER_SPAWN_EGG, this));
    }
}
