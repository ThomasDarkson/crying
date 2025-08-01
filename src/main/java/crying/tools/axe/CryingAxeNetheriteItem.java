package crying.tools.axe;

import crying.tools.CryingToolItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingAxeNetheriteItem extends AbstractCryingAxeItem {
    public CryingAxeNetheriteItem() {
        super(CryingToolItem.NETHERITE_DURABILITY, CryingToolItem.NETHERITE_AXE_SPEED, CryingToolItem.NETHERITE_AXE_ATTACK_DAMAGE_BONUS, CryingToolItem.NETHERITE_ENCHANTABILITY, "crying_axe_netherite");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.NETHERITE_INGOT;
    }
}
