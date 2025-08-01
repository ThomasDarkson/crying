package crying.tools.shovel;

import crying.tools.CryingToolItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingShovelNetheriteItem extends AbstractCryingShovelItem {
    public CryingShovelNetheriteItem() {
        super(CryingToolItem.NETHERITE_DURABILITY, CryingToolItem.NETHERITE_SPEED, CryingToolItem.NETHERITE_SHOVEL_ATTACK_DAMAGE_BONUS, CryingToolItem.NETHERITE_ENCHANTABILITY, "crying_shovel_netherite");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.NETHERITE_INGOT;
    }
}
