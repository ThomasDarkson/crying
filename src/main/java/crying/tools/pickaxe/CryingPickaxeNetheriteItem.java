package crying.tools.pickaxe;

import crying.tools.CryingToolItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingPickaxeNetheriteItem extends AbstractCryingPickaxeItem {
    public CryingPickaxeNetheriteItem() {
        super(CryingToolItem.NETHERITE_DURABILITY, NETHERITE_SPEED, NETHERITE_PICKAXE_ATTACK_DAMAGE_BONUS, NETHERITE_ENCHANTABILITY, "crying_pickaxe_netherite");
    }

    @Override
    public Item getCoreIngredient() {
        return Items.NETHERITE_INGOT;
    }
}
