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

public class CryingLeggingsItem extends Item {    
    public CryingLeggingsItem()
    {
        super(new Item.Properties()
            .humanoidArmor(CryingArmor.CRYING_ARMOR_MATERIAL, ArmorType.LEGGINGS)
            .fireResistant()
            .enchantable(50)
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_leggings")))
            .durability(ArmorType.LEGGINGS.getDurability(591))
        );
        Crying.register(this, "crying_leggings");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_BOOTS, this));
    }
}
