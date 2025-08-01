package crying.tools.hoe;

import crying.tools.CryingToolItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingHoeGoldItem extends AbstractCryingHoeItem {
    public CryingHoeGoldItem() {
        super(CryingToolItem.GOLD_DURABILITY, CryingToolItem.GOLD_SPEED, CryingToolItem.GOLD_HOE_ATTACK_DAMAGE_BONUS, CryingToolItem.GOLD_ENCHANTABILITY, "crying_hoe_gold");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.GOLD_INGOT;
    }
}