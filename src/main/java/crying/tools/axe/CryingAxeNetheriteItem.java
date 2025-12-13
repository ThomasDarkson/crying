package crying.tools.axe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingAxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CryingAxeNetheriteItem extends AbstractCryingAxeItem {
    public CryingAxeNetheriteItem() {
        super(CryingToolItem.NETHERITE_DURABILITY, CryingToolItem.NETHERITE_AXE_SPEED, CryingToolItem.NETHERITE_ENCHANTABILITY, "crying_axe_netherite");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.NETHERITE_INGOT;
    }
}
