package crying.tools;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class CryingShieldItem extends Item {
    public CryingShieldItem() {
        super(new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_shield")))
            .fireproof()
            .repairable(Crying.CRYING_INGOT)
            .maxDamage(2688));

        Crying.register(this, "crying_shield");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.SHIELD, this));
    }

    public void useShield(ItemStack stack, Hand hand, PlayerEntity player, Entity attacker, DamageSource source, int damage) {
        if (damage > 3)
            stack.damage(damage, player);
        else
            stack.damage(1, player);

        if (attacker instanceof LivingEntity entity && !entity.isDead() && player.isSneaking()) {
            entity.damage((ServerWorld) player.getEntityWorld(), new DamageSource(source.getTypeRegistryEntry(), player), Math.round(damage / 2));
        }

        player.getEntityWorld().playSound(null, player.getBlockPos(), SoundEvents.ITEM_SHIELD_BLOCK.value(), player.getSoundCategory());
        if (stack.getDamage() >= stack.getMaxDamage()) {
            player.getEntityWorld().playSound(null, player.getBlockPos(), SoundEvents.ITEM_SHIELD_BREAK.value(), player.getSoundCategory());
        }
    }
}
