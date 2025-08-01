package crying.armors;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class CryingHelmetItem extends Item {
    public CryingHelmetItem()
    {
        super(new Item.Settings()
            .armor(CryingArmor.CRYING_ARMOR_MATERIAL, EquipmentType.HELMET)
            .fireproof()
            .enchantable(50)
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_helmet")))
            .maxDamage(EquipmentType.HELMET.getMaxDamage(591))
        );
            
        Crying.register(this, "crying_helmet");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_BOOTS, this));
    }
}
