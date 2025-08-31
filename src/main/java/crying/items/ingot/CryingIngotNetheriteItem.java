package crying.items.ingot;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingIngotNetheriteItem extends AbstractCryingIngotItem {
    public CryingIngotNetheriteItem() {
        super("crying_ingot_netherite");
    }

    @Override
    public Item getInfusedItem() {
        return Items.NETHERITE_INGOT;
    }
}
