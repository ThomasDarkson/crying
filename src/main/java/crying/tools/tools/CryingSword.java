package crying.tools.tools;

import crying.tools.Crying;
import crying.tools.other.CryingTags;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class CryingSword {
    public int durability = 16247;

    public float speed = 18F;

    public float attackDamageBonus = 0F;

    public int enchantability = 120;

    public SwordItem item = null;

    ToolMaterial material = null; 

    public CryingSword() {
        create();
        Crying.register(item, "crying_sword");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_SWORD, item));
    }

    public void create() {
        this.material = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, attackDamageBonus, enchantability, CryingTags.CryingTag);
        this.item = new SwordItem(material, 19F, -2.4F, new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.MOD_ID, "crying_sword")))
            .fireproof()
            .rarity(Rarity.RARE));
    }
}
