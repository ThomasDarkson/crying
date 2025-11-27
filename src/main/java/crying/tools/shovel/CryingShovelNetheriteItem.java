package crying.tools.shovel;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingShovelItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingShovelNetheriteItem extends AbstractCryingShovelItem {
    public CryingShovelNetheriteItem() {
        super(CryingToolItem.NETHERITE_DURABILITY, CryingToolItem.NETHERITE_SPEED, CryingToolItem.NETHERITE_ENCHANTABILITY, "crying_shovel_netherite");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.NETHERITE_INGOT;
    }
}
