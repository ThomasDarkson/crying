package crying.tools.abstracts.copper;

import crying.Crying;
import crying.enums.ToolType;
import crying.other.CryingTags;
import crying.tools.CryingToolItem;
import crying.tools.OxidizableCryingToolItem;
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

public class AbstractCryingPickaxeCopperItem extends OxidizableCryingToolItem {
    public AbstractCryingPickaxeCopperItem(int durability, float speed, int enchantable, String id) {
        super(new Item.Properties()
            .pickaxe(new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, CryingToolItem.PICKAXE_ATTACK_DAMAGE_BONUS, enchantable, CryingTags.CryingTag), 6F, -2.8F)
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Crying.ID, id)))
            .rarity(id.equals("crying_pickaxe_over-hardened_core_with_eye") ? Rarity.EPIC : Rarity.COMMON));

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_PICKAXE, this));
    }

    @Override
    public ToolType getToolType() {
        return ToolType.PICKAXE;
    }
}
