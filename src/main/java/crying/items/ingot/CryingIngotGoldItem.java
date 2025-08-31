package crying.items.ingot;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CryingIngotGoldItem extends AbstractCryingIngotItem {
    public CryingIngotGoldItem() {
        super("crying_ingot_gold");
    }

    @Override
    public Item getInfusedItem() {
        return Items.GOLD_INGOT;
    }
}
