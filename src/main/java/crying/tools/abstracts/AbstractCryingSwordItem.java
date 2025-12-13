package crying.tools.abstracts;

import crying.Crying;
import crying.enums.ToolType;
import crying.other.CryingTags;
import crying.tools.CryingToolItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;

public class AbstractCryingSwordItem extends CryingToolItem {
    public AbstractCryingSwordItem(int durability, float speed, int enchantable, String id) {
        super(new Item.Properties()
            .sword(new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, CryingToolItem.SWORD_ATTACK_DAMAGE_BONUS, enchantable, CryingTags.CryingTag), 8F, -2.4F)
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Crying.ID, id)))
            .rarity(id.equals("criers_sword") ? Rarity.EPIC : Rarity.COMMON)
        );

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_SWORD, this));
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SWORD;
    }
}
