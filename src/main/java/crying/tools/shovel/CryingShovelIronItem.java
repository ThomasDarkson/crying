package crying.tools.shovel;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingShovelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CryingShovelIronItem extends AbstractCryingShovelItem {
    public CryingShovelIronItem() {
        super(CryingToolItem.IRON_DURABILITY, CryingToolItem.IRON_SPEED, CryingToolItem.IRON_ENCHANTABILITY, "crying_shovel_iron");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.IRON_INGOT;
    }
}
