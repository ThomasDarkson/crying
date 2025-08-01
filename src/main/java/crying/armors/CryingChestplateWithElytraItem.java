package crying.armors;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class CryingChestplateWithElytraItem extends Item {    
    public CryingChestplateWithElytraItem()
    {
        super(new Item.Settings()
            .armor(CryingArmor.CRYING_WITH_ELYTRA_ARMOR_MATERIAL, EquipmentType.CHESTPLATE)
            .fireproof()
            .enchantable(50)
            .rarity(Rarity.EPIC)
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_chestplate_with_elytra")))
            .maxDamage(EquipmentType.CHESTPLATE.getMaxDamage(618))
        );
        Crying.register(this, "crying_chestplate_with_elytra");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Crying.CRYING_CHESTPLATE, this));
    }
}
