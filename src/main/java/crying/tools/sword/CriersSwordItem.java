package crying.tools.sword;

import crying.Crying;
import crying.tools.abstracts.AbstractCryingSwordItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class CriersSwordItem extends AbstractCryingSwordItem {
    public CriersSwordItem() {
        super(OVER_HARDENED_CORE_WITH_EYE_DURABILITY, OVER_HARDENED_CORE_WITH_EYE_ENCHANTABILITY, OVER_HARDENED_CORE_WITH_EYE_ENCHANTABILITY, "criers_sword");
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.isSneaking())
            target.dropItem((ServerWorld) target.getWorld(), Items.GOLD_INGOT);

        if (!target.isDead()) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 1));
        }
    }

    @Override
    public Item getCoreIngredient() {
        return Crying.OVER_HARDENED_CORE_WITH_EYE.asItem();
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable("item.crying.criers_sword");
    }
}
