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

public class CryingLeggingsItem extends Item {    
    public CryingLeggingsItem()
    {
        super(new Item.Settings()
            .armor(CryingArmor.CRYING_ARMOR_MATERIAL, EquipmentType.LEGGINGS)
            .fireproof()
            .enchantable(50)
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_leggings")))
            .maxDamage(EquipmentType.LEGGINGS.getMaxDamage(591))
        );
        Crying.register(this, "crying_leggings");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_BOOTS, this));
    }
}
