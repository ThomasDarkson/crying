package crying.tools.hoe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingHoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingHoeNetheriteItem extends AbstractCryingHoeItem {
    public CryingHoeNetheriteItem() {
        super(CryingToolItem.NETHERITE_DURABILITY, CryingToolItem.NETHERITE_SPEED, CryingToolItem.NETHERITE_ENCHANTABILITY, "crying_hoe_netherite");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.NETHERITE_INGOT;
    }
}