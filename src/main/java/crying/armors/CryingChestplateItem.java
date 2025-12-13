package crying.armors;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.ArmorType;

public class CryingChestplateItem extends Item {    
    public CryingChestplateItem()
    {
        super(new Item.Properties()
            .humanoidArmor(CryingArmor.CRYING_ARMOR_MATERIAL, ArmorType.CHESTPLATE)
            .fireResistant()
            .enchantable(50)
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_chestplate")))
            .durability(ArmorType.CHESTPLATE.getDurability(591))
        );

        Crying.register(this, "crying_chestplate");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_BOOTS, this));
    }
}
