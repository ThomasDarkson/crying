package crying.tools.pickaxe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingPickaxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CryingPickaxeIronItem extends AbstractCryingPickaxeItem {
    public CryingPickaxeIronItem() {
        super(CryingToolItem.IRON_DURABILITY, IRON_SPEED, IRON_ENCHANTABILITY, "crying_pickaxe_iron");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.IRON_INGOT;
    }
}
