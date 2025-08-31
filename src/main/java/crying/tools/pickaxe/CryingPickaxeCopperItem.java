package crying.tools.pickaxe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.copper.AbstractCryingPickaxeCopperItem;

public class CryingPickaxeCopperItem extends AbstractCryingPickaxeCopperItem {
    public CryingPickaxeCopperItem() {
        super(CryingToolItem.COPPER_DURABILITY, COPPER_SPEED, COPPER_PICKAXE_ATTACK_DAMAGE_BONUS, COPPER_ENCHANTABILITY, "crying_pickaxe_copper");
    }
}
