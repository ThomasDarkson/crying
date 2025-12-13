package crying.armors;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorType;

public class CryingChestplateWithElytraItem extends Item {    
    public CryingChestplateWithElytraItem()
    {
        super(new Item.Properties()
            .humanoidArmor(CryingArmor.CRYING_WITH_ELYTRA_ARMOR_MATERIAL, ArmorType.CHESTPLATE)
            .fireResistant()
            .enchantable(50)
            .rarity(Rarity.EPIC)
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_chestplate_with_elytra")))
            .durability(ArmorType.CHESTPLATE.getDurability(618))
        );
        Crying.register(this, "crying_chestplate_with_elytra");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register((itemGroup) -> itemGroup.addAfter(Crying.CRYING_CHESTPLATE, this));
    }
}
