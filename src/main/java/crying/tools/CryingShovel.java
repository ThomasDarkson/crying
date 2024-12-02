package crying.tools;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class CryingShovel {
    public int durability = 16247;

    public float speed = 32F;

    public float attackDamageBonus = 0F;

    public int enchantability = 120;

    public ShovelItem item = null;

    ToolMaterial material = null; 

    public CryingShovel() {
        create();
        CryingTools.register(item, "crying_shovel");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_SHOVEL, item));
    }

    public void create() {
        this.material = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, attackDamageBonus, enchantability, CryingTags.CryingTag);
        this.item = new ShovelItem(material, 6.5F, -3F, new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(CryingTools.MOD_ID, "crying_shovel")))
            .fireproof()
            .rarity(Rarity.RARE));
    }
}
