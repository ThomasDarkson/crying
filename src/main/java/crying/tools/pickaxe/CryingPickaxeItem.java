package crying.tools.pickaxe;

import crying.Crying;
import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingPickaxeItem;
import net.minecraft.world.item.Item;

public class CryingPickaxeItem extends AbstractCryingPickaxeItem {
    public CryingPickaxeItem() {
        super(CryingToolItem.CRYING_DURABILITY, CRYING_SPEED, CRYING_ENCHANTABILITY, "crying_pickaxe");
    }

    @Override
    public Item getCoreIngredient() {
        return Crying.CRYING_INGOT;
    }
}
