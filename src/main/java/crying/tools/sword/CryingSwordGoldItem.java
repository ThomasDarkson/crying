package crying.tools.sword;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;

public class CryingSwordGoldItem extends AbstractCryingSwordItem {
    public CryingSwordGoldItem() {
        super(GOLD_DURABILITY, GOLD_SPEED, GOLD_SWORD_ATTACK_DAMAGE_BONUS, GOLD_ENCHANTABILITY, "crying_sword_gold");
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.isSneaking())
            target.dropItem((ServerWorld) target.getWorld(), Items.GOLD_INGOT);
    }

    @Override
    public Item getCoreIngredient() {
        return Items.GOLD_INGOT;
    }
}
