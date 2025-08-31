package crying.tools.hoe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.copper.AbstractCryingHoeCopperItem;

public class CryingHoeCopperItem extends AbstractCryingHoeCopperItem {
    public CryingHoeCopperItem() {
        super(CryingToolItem.COPPER_DURABILITY, CryingToolItem.COPPER_SPEED, CryingToolItem.COPPER_HOE_ATTACK_DAMAGE_BONUS, CryingToolItem.COPPER_ENCHANTABILITY, "crying_hoe_copper");
    }
}