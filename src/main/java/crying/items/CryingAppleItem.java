package crying.items;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Items;

public class CryingAppleItem extends CryingFoodItem {
    public CryingAppleItem() {
        super("crying_apple", new FoodComponent(4, 10.3F, true), null, Items.APPLE, 30F, false);
    }

    @Override
    public int restoresSanity() {
        return 4;
    }
}
