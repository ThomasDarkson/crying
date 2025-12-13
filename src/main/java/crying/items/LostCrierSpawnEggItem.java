package crying.items;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public class LostCrierSpawnEggItem extends SpawnEggItem {
    public LostCrierSpawnEggItem() {
        super(Crying.LOST_CRIER, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "lost_crier_spawn_egg"))));

        Crying.register(this, "lost_crier_spawn_egg");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register((itemGroup) -> itemGroup.addAfter(Crying.CRIER_SPAWN_EGG, this));
    }
}
