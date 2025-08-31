package crying.items.ingot;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingIngotCopperItem extends AbstractCryingIngotItem {
    public CryingIngotCopperItem() {
        super("crying_ingot_copper");
    }

    @Override
    public Item getInfusedItem() {
        return Items.COPPER_INGOT;
    }
}
