package crying.tools.sword;

import crying.tools.abstracts.AbstractCryingSwordItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CryingSwordGoldItem extends AbstractCryingSwordItem {
    public CryingSwordGoldItem() {
        super(GOLD_DURABILITY, GOLD_SPEED, GOLD_ENCHANTABILITY, "crying_sword_gold");
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.isShiftKeyDown())
            target.spawnAtLocation((ServerLevel) target.level(), Items.GOLD_INGOT);
    }

    @Override
    public Item getCoreIngredient() {
        return Items.GOLD_INGOT;
    }
}
