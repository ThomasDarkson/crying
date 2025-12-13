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

public class CryingHorseArmor extends Item {    
    public CryingHorseArmor()
    {
        super(new Item.Properties()
            .horseArmor(CryingArmor.CRYING_ARMOR_MATERIAL)
            .fireResistant()
            .enchantable(50)
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_horse_armor")))
            .durability(ArmorType.BODY.getDurability(591))
        );
        Crying.register(this, "crying_horse_armor");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.DIAMOND_HORSE_ARMOR, this));
    }
}
