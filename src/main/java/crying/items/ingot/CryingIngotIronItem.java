package crying.items.ingot;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingIngotIronItem extends AbstractCryingIngotItem {
    public CryingIngotIronItem() {
        super("crying_ingot_iron");
    }

    @Override
    public Item getInfusedItem() {
        return Items.IRON_INGOT;
    }
}
