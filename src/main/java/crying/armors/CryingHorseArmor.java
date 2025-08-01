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

public class CryingHorseArmor extends Item {    
    public CryingHorseArmor()
    {
        super(new Item.Settings()
            .horseArmor(CryingArmor.CRYING_ARMOR_MATERIAL)
            .fireproof()
            .enchantable(50)
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_horse_armor")))
            .maxDamage(EquipmentType.BODY.getMaxDamage(591))
        );
        Crying.register(this, "crying_horse_armor");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.DIAMOND_HORSE_ARMOR, this));
    }
}
