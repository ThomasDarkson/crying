package crying.tools.axe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingAxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingAxeGoldItem extends AbstractCryingAxeItem {
    public CryingAxeGoldItem() {
        super(CryingToolItem.GOLD_DURABILITY, CryingToolItem.GOLD_AXE_SPEED, CryingToolItem.GOLD_ENCHANTABILITY, "crying_axe_gold");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.GOLD_INGOT;
    }
}
