package crying.tools.sword;

import crying.tools.abstracts.AbstractCryingSwordItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingSwordIronItem extends AbstractCryingSwordItem {
    public CryingSwordIronItem() {
        super(IRON_DURABILITY, IRON_SPEED, IRON_SWORD_ATTACK_DAMAGE_BONUS, IRON_ENCHANTABILITY, "crying_sword_iron");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.IRON_INGOT;
    }
}
