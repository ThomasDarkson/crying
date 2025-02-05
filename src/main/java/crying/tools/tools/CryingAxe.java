package crying.tools.tools;

import crying.tools.Crying;
import crying.tools.other.CryingTags;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.AxeItem;
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

public class CryingAxe extends AxeItem {
    private static int durability = 14622;

    private static float speed = 60F;

    private static float attackDamageBonus = 0F;

    private static int enchantability = 50;

    public CryingAxe() {
        super(new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, attackDamageBonus, enchantability, CryingTags.CryingTag), 24, -3F, new Item.Settings()
        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.MOD_ID, "crying_axe")))
        .fireproof()
        .enchantable(enchantability));
        
        Crying.register(this, "crying_axe");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_AXE, this));
    }

    @Override
    public Text getName(ItemStack stack) {
        int damage = (stack.getComponents().get(DataComponentTypes.DAMAGE));
        if (damage >= (9748)) {
            return Text.translatable("item.crying.crying_axe_heavily_damaged");
        }
        if (damage >= (4874))
        {
            return Text.translatable("item.crying.crying_axe_damaged");
        }
        return super.getName(stack);
    }
}
