package crying.tools.hoe;

import crying.Crying;
import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingHoeItem;
import net.minecraft.item.Item;

public class CryingHoeItem extends AbstractCryingHoeItem {
    public CryingHoeItem() {
        super(CryingToolItem.CRYING_DURABILITY, CryingToolItem.CRYING_SPEED, CryingToolItem.CRYING_HOE_ATTACK_DAMAGE_BONUS, CryingToolItem.CRYING_ENCHANTABILITY, "crying_hoe");
    }

    @Override
    public Item getCoreIngredient() {
        return Crying.CRYING_INGOT;
    }
}