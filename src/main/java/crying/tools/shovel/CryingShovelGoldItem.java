package crying.tools.shovel;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingShovelItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingShovelGoldItem extends AbstractCryingShovelItem {
    public CryingShovelGoldItem() {
        super(CryingToolItem.GOLD_DURABILITY, CryingToolItem.GOLD_SPEED, CryingToolItem.GOLD_SHOVEL_ATTACK_DAMAGE_BONUS, CryingToolItem.GOLD_ENCHANTABILITY, "crying_shovel_gold");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.GOLD_INGOT;
    }
}
