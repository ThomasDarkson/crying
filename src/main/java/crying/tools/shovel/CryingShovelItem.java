package crying.tools.shovel;

import crying.Crying;
import crying.tools.CryingToolItem;
import net.minecraft.item.Item;

public class CryingShovelItem extends AbstractCryingShovelItem {
    public CryingShovelItem() {
        super(CryingToolItem.CRYING_DURABILITY, CryingToolItem.CRYING_SPEED, CryingToolItem.CRYING_SHOVEL_ATTACK_DAMAGE_BONUS, CryingToolItem.CRYING_ENCHANTABILITY, "crying_shovel");
    }

    @Override
    public Item getCoreIngredient() {
        return Crying.CRYING_INGOT;
    }
}
