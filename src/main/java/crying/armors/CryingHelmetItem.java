package crying.armors;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.ArmorType;

public class CryingHelmetItem extends Item {
    public CryingHelmetItem()
    {
        super(new Item.Properties()
            .humanoidArmor(CryingArmor.CRYING_ARMOR_MATERIAL, ArmorType.HELMET)
            .fireResistant()
            .enchantable(50)
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Crying.ID, "crying_helmet")))
            .durability(ArmorType.HELMET.getDurability(591))
        );
            
        Crying.register(this, "crying_helmet");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_BOOTS, this));
    }
}
