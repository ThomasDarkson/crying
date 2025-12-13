package crying.items;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;

public class CryingAppleItem extends CryingFoodItem {
    public CryingAppleItem() {
        super("crying_apple", new FoodProperties(4, 10.3F, true), null, Items.APPLE, 30F, false);
    }

    @Override
    public int restoresSanity() {
        return 4;
    }
}
