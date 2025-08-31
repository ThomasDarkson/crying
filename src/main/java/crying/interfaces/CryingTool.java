package crying.interfaces;

import crying.enums.ToolType;
import net.minecraft.item.Item;

public interface CryingTool {
    Item getCoreIngredient();
    ToolType getToolType();
}
