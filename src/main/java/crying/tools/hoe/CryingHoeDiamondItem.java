package crying.tools.hoe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingHoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingHoeDiamondItem extends AbstractCryingHoeItem {
    public CryingHoeDiamondItem() {
        super(CryingToolItem.DIAMOND_DURABILITY, CryingToolItem.DIAMOND_SPEED, CryingToolItem.DIAMOND_HOE_ATTACK_DAMAGE_BONUS, CryingToolItem.DIAMOND_ENCHANTABILITY, "crying_hoe_diamond");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.DIAMOND;
    }
}