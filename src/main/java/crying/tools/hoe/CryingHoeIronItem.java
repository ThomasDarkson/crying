package crying.tools.hoe;

import crying.tools.CryingToolItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingHoeIronItem extends AbstractCryingHoeItem {
    public CryingHoeIronItem() {
        super(CryingToolItem.IRON_DURABILITY, CryingToolItem.IRON_SPEED, CryingToolItem.IRON_HOE_ATTACK_DAMAGE_BONUS, CryingToolItem.IRON_ENCHANTABILITY, "crying_hoe_iron");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.IRON_INGOT;
    }
}