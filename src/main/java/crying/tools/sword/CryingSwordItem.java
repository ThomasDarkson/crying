package crying.tools.sword;

import crying.Crying;
import crying.tools.abstracts.AbstractCryingSwordItem;
import net.minecraft.item.Item;

public class CryingSwordItem extends AbstractCryingSwordItem {
    public CryingSwordItem() {
        super(CRYING_DURABILITY, CRYING_SPEED, CRYING_ENCHANTABILITY, "crying_sword");
    }

    @Override
    public Item getCoreIngredient() {
        return Crying.CRYING_INGOT;
    }
}
