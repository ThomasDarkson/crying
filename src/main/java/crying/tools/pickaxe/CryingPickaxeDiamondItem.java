package crying.tools.pickaxe;

import crying.tools.CryingToolItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingPickaxeDiamondItem extends AbstractCryingPickaxeItem {
    public CryingPickaxeDiamondItem() {
        super(CryingToolItem.DIAMOND_DURABILITY, DIAMOND_SPEED, DIAMOND_PICKAXE_ATTACK_DAMAGE_BONUS, DIAMOND_ENCHANTABILITY, "crying_pickaxe_diamond");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.DIAMOND;
    }
}
