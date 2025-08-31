package crying.items.ingot;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingIngotDiamondItem extends AbstractCryingIngotItem {
    public CryingIngotDiamondItem() {
        super("crying_ingot_diamond");
    }

    @Override
    public Item getInfusedItem() {
        return Items.DIAMOND;
    }
}
