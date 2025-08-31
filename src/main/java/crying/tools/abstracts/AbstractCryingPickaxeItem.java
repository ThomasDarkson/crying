package crying.tools.abstracts;

import crying.Crying;
import crying.enums.ToolType;
import crying.other.CryingTags;
import crying.tools.CryingToolItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class AbstractCryingPickaxeItem extends CryingToolItem {
    public AbstractCryingPickaxeItem(int durability, float speed, float bonus, int enchantable, String id) {
        super(new Item.Settings()
            .pickaxe(new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, bonus, enchantable, CryingTags.CryingTag), 6F, -2.8F)
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, id)))
            .rarity(id.equals("crying_pickaxe_over-hardened_core_with_eye") ? Rarity.EPIC : Rarity.COMMON));

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_PICKAXE, this));
    }

    @Override
    public ToolType getToolType() {
        return ToolType.PICKAXE;
    }
}
