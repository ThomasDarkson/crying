package crying.tools.axe;

import crying.Crying;
import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingAxeItem;
import net.minecraft.item.Item;

public class CryingAxeItem extends AbstractCryingAxeItem {
    public CryingAxeItem() {
        super(CryingToolItem.CRYING_DURABILITY, CryingToolItem.CRYING_AXE_SPEED, CryingToolItem.CRYING_ENCHANTABILITY, "crying_axe");
    }

    @Override
    public Item getCoreIngredient() {
        return Crying.CRYING_INGOT;
    }
}
