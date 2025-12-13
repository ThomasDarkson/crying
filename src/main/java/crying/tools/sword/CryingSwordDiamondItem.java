package crying.tools.sword;

import crying.tools.abstracts.AbstractCryingSwordItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CryingSwordDiamondItem extends AbstractCryingSwordItem {
    public CryingSwordDiamondItem() {
        super(DIAMOND_DURABILITY, DIAMOND_SPEED, DIAMOND_ENCHANTABILITY, "crying_sword_diamond");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.DIAMOND;
    }
}
