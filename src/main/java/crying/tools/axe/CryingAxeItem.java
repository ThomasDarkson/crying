package crying.tools.axe;

import crying.Crying;
import crying.tools.CryingToolItem;
import net.minecraft.item.Item;

public class CryingAxeItem extends AbstractCryingAxeItem {
    public CryingAxeItem() {
        super(CryingToolItem.CRYING_DURABILITY, CryingToolItem.CRYING_AXE_SPEED, CryingToolItem.CRYING_AXE_ATTACK_DAMAGE_BONUS, CryingToolItem.CRYING_ENCHANTABILITY, "crying_axe");
    }

    @Override
    public Item getCoreIngredient() {
        return Crying.CRYING_INGOT;
    }
}
