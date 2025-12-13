package crying.tools.pickaxe;

import crying.tools.CryingToolItem;
import crying.tools.abstracts.AbstractCryingPickaxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CryingPickaxeNetheriteItem extends AbstractCryingPickaxeItem {
    public CryingPickaxeNetheriteItem() {
        super(CryingToolItem.NETHERITE_DURABILITY, NETHERITE_SPEED, NETHERITE_ENCHANTABILITY, "crying_pickaxe_netherite");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.NETHERITE_INGOT;
    }
}
