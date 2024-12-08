package crying.tools.armors;

import crying.tools.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class CryingChestplate {
    public static final Item item = new ArmorItem(
            CryingArmor.CRYING_ARMOR_MATERIAL, 
            EquipmentType.CHESTPLATE, 
            new Item.Settings()
            .fireproof()
            .enchantable(100)
            .rarity(Rarity.RARE)
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.MOD_ID, "crying_chestplate")))
            .maxDamage(EquipmentType.CHESTPLATE.getMaxDamage(32)));
    
    public CryingChestplate()
    {
        Crying.register(item, "crying_chestplate");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_BOOTS, item));
    }
}
