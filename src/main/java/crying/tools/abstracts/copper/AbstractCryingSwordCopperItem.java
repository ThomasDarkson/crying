package crying.tools.abstracts.copper;

import crying.Crying;
import crying.enums.ToolType;
import crying.other.CryingTags;
import crying.tools.CryingToolItem;
import crying.tools.OxidizableCryingToolItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;

public class AbstractCryingSwordCopperItem extends OxidizableCryingToolItem {
    public AbstractCryingSwordCopperItem(int durability, float speed, int enchantable, String id) {
        super(new Item.Properties()
            .sword(new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, CryingToolItem.SWORD_ATTACK_DAMAGE_BONUS, enchantable, CryingTags.CryingTag), 8F, -2.4F)
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, id)))
        );

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_SWORD, this));
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SWORD;
    }
}
