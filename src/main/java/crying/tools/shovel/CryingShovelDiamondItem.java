package crying.tools.shovel;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingShovelItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingShovelDiamondItem extends AbstractCryingShovelItem {
    public CryingShovelDiamondItem() {
        super(CryingToolItem.DIAMOND_DURABILITY, CryingToolItem.DIAMOND_SPEED, CryingToolItem.DIAMOND_ENCHANTABILITY, "crying_shovel_diamond");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.DIAMOND;
    }
}
