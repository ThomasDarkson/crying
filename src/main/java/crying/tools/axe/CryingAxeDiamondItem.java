package crying.tools.axe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingAxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingAxeDiamondItem extends AbstractCryingAxeItem {
    public CryingAxeDiamondItem() {
        super(CryingToolItem.DIAMOND_DURABILITY, CryingToolItem.DIAMOND_AXE_SPEED, CryingToolItem.DIAMOND_ENCHANTABILITY, "crying_axe_diamond");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.DIAMOND;
    }
}
