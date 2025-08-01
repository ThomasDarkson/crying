package crying.tools.sword;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingSwordDiamondItem extends AbstractCryingSwordItem {
    public CryingSwordDiamondItem() {
        super(DIAMOND_DURABILITY, DIAMOND_SPEED, DIAMOND_SWORD_ATTACK_DAMAGE_BONUS, DIAMOND_ENCHANTABILITY, "crying_sword_diamond");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.DIAMOND;
    }
}
