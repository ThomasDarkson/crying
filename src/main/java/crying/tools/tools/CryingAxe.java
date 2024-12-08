package crying.tools.tools;

import crying.tools.Crying;
import crying.tools.other.CryingTags;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class CryingAxe {
    public int durability = 32495;

    public float speed = 90F;

    public float attackDamageBonus = 0F;

    public int enchantability = 120;

    public AxeItem item = null;

    ToolMaterial material = null; 

    public CryingAxe() {
        create();
        Crying.register(item, "crying_axe");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_AXE, item));
    }

    public void create() {
        this.material = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, attackDamageBonus, enchantability, CryingTags.CryingTag);
        this.item = new AxeItem(material, 29, -3F, new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.MOD_ID, "crying_axe")))
            .fireproof()
            .rarity(Rarity.RARE));
    }
}
