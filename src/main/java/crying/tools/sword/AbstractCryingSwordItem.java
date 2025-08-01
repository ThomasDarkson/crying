package crying.tools.sword;

import crying.Crying;
import crying.other.CryingTags;
import crying.tools.CryingToolItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class AbstractCryingSwordItem extends CryingToolItem {
    public AbstractCryingSwordItem(int durability, float speed, float bonus, int enchantable, String id) {
        super(new Item.Settings()
            .sword(new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, bonus, enchantable, CryingTags.CryingTag), 14F, -2.4F)
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, id)))
            .rarity(id.equals("criers_sword") ? Rarity.EPIC : Rarity.COMMON)
        );

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_SWORD, this));
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable("item.crying.crying_sword");
    }
}
