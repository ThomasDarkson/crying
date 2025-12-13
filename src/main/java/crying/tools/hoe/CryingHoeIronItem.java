package crying.tools.hoe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingHoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CryingHoeIronItem extends AbstractCryingHoeItem {
    public CryingHoeIronItem() {
        super(CryingToolItem.IRON_DURABILITY, CryingToolItem.IRON_SPEED, CryingToolItem.IRON_ENCHANTABILITY, "crying_hoe_iron");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.IRON_INGOT;
    }
}