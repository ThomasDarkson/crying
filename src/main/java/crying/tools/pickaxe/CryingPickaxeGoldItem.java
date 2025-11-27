package crying.tools.pickaxe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingPickaxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingPickaxeGoldItem extends AbstractCryingPickaxeItem {
    public CryingPickaxeGoldItem() {
        super(CryingToolItem.GOLD_DURABILITY, GOLD_SPEED, GOLD_ENCHANTABILITY, "crying_pickaxe_gold");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.GOLD_INGOT;
    }
}
