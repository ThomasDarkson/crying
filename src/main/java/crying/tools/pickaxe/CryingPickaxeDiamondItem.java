package crying.tools.pickaxe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingPickaxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CryingPickaxeDiamondItem extends AbstractCryingPickaxeItem {
    public CryingPickaxeDiamondItem() {
        super(CryingToolItem.DIAMOND_DURABILITY, DIAMOND_SPEED, DIAMOND_ENCHANTABILITY, "crying_pickaxe_diamond");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.DIAMOND;
    }
}
