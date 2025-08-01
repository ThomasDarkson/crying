package crying.tools.axe;

import crying.tools.CryingToolItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingAxeGoldItem extends AbstractCryingAxeItem {
    public CryingAxeGoldItem() {
        super(CryingToolItem.GOLD_DURABILITY, CryingToolItem.GOLD_AXE_SPEED, CryingToolItem.GOLD_AXE_ATTACK_DAMAGE_BONUS, CryingToolItem.GOLD_ENCHANTABILITY, "crying_axe_gold");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.GOLD_INGOT;
    }
}
