package crying.tools.sword;

import crying.Crying;
import crying.entities.GranterEntity;
import crying.tools.abstracts.AbstractCryingSwordItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CriersSwordItem extends AbstractCryingSwordItem {
    public CriersSwordItem() {
        super(OVER_HARDENED_CORE_WITH_EYE_DURABILITY, OVER_HARDENED_CORE_WITH_EYE_ENCHANTABILITY, OVER_HARDENED_CORE_WITH_EYE_ENCHANTABILITY, "criers_sword");
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.isShiftKeyDown())
            target.spawnAtLocation((ServerLevel) target.level(), Items.GOLD_INGOT);

        if (!target.isDeadOrDying()) {
            GranterEntity.summonEvilGranterEntity(target.level(), target);
        }
    }

    @Override
    public Item getCoreIngredient() {
        return Crying.OVER_HARDENED_CORE_WITH_EYE.asItem();
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.crying.criers_sword");
    }
}
