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

public class AbstractCryingSwordItem extends CryingToolItem {
    public AbstractCryingSwordItem(int durability, float speed, int enchantable, String id) {
        super(new Item.Settings()
            .sword(new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, CryingToolItem.SWORD_ATTACK_DAMAGE_BONUS, enchantable, CryingTags.CryingTag), 8F, -2.4F)
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, id)))
            .rarity(id.equals("criers_sword") ? Rarity.EPIC : Rarity.COMMON)
        );

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_SWORD, this));
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SWORD;
    }
}
