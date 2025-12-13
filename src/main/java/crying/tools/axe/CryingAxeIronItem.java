package crying.tools.axe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingAxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CryingAxeIronItem extends AbstractCryingAxeItem {
    public CryingAxeIronItem() {
        super(CryingToolItem.IRON_DURABILITY, CryingToolItem.IRON_AXE_SPEED, CryingToolItem.IRON_ENCHANTABILITY, "crying_axe_iron");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.IRON_INGOT;
    }
}
