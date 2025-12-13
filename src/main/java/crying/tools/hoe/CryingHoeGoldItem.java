package crying.tools.hoe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingHoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CryingHoeGoldItem extends AbstractCryingHoeItem {
    public CryingHoeGoldItem() {
        super(CryingToolItem.GOLD_DURABILITY, CryingToolItem.GOLD_SPEED, CryingToolItem.GOLD_ENCHANTABILITY, "crying_hoe_gold");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.GOLD_INGOT;
    }
}