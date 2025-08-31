package crying.tools.sword;

import crying.tools.abstracts.AbstractCryingSwordItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingSwordNetheriteItem extends AbstractCryingSwordItem {
    public CryingSwordNetheriteItem() {
        super(NETHERITE_DURABILITY, NETHERITE_SPEED, NETHERITE_SWORD_ATTACK_DAMAGE_BONUS, NETHERITE_ENCHANTABILITY, "crying_sword_netherite");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.NETHERITE_INGOT;
    }
}
