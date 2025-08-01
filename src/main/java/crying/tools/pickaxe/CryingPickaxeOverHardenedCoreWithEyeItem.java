package crying.tools.pickaxe;

import crying.Crying;
import crying.tools.CryingToolItem;
import net.minecraft.item.Item;

public class CryingPickaxeOverHardenedCoreWithEyeItem extends AbstractCryingPickaxeItem {
    public CryingPickaxeOverHardenedCoreWithEyeItem() {
        super(CryingToolItem.OVER_HARDENED_CORE_WITH_EYE_DURABILITY, OVER_HARDENED_CORE_WITH_EYE_SPEED, OVER_HARDENED_CORE_WITH_EYE_PICKAXE_ATTACK_DAMAGE_BONUS, OVER_HARDENED_CORE_WITH_EYE_ENCHANTABILITY, "crying_pickaxe_over-hardened_core_with_eye");
    }

    @Override
    public Item getCoreIngredient() {
        return Crying.OVER_HARDENED_CORE_WITH_EYE.asItem();
    }
}
