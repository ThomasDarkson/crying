package crying.tools.sword;

import crying.Crying;
import net.minecraft.item.Item;

public class CryingSwordItem extends AbstractCryingSwordItem {
    public CryingSwordItem() {
        super(CRYING_DURABILITY, CRYING_SPEED, CRYING_SWORD_ATTACK_DAMAGE_BONUS, CRYING_ENCHANTABILITY, "crying_sword");
    }

    @Override
    public Item getCoreIngredient() {
        return Crying.CRYING_INGOT;
    }
}
