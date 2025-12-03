package crying.tools;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
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
import net.minecraft.util.Rarity;

public class CryingShieldItem extends Item {
    public static final int CRYING_SHIELD_HEALTH = 2688;

    public CryingShieldItem() {
        super(new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_shield")))
            .fireproof()
            .rarity(Rarity.EPIC)
            .attributeModifiers(AttributeModifiersComponent.builder().add(EntityAttributes.KNOCKBACK_RESISTANCE, new EntityAttributeModifier(Identifier.of(Crying.ID, ""), 0.200000000000000618d, Operation.ADD_VALUE), AttributeModifierSlot.HAND).build())
            .maxDamage(CRYING_SHIELD_HEALTH));

        Crying.register(this, "crying_shield");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.SHIELD, this));
    }

    public void useShield(ItemStack stack, Hand hand, PlayerEntity player, Entity attacker, DamageSource source, int damage) {
        if (damage > 3)
            stack.damage(damage, player);
        else
            stack.damage(1, player);

        if (attacker != null && attacker instanceof LivingEntity entity && !entity.isDead() && player.isSneaking()) {
            entity.damage((ServerWorld) player.getEntityWorld(), new DamageSource(source.getTypeRegistryEntry(), player), Math.round(damage / 2));
        }

        player.getEntityWorld().playSound(null, player.getBlockPos(), SoundEvents.ITEM_SHIELD_BLOCK.value(), player.getSoundCategory());
        if (stack.getDamage() >= stack.getMaxDamage()) {
            player.getEntityWorld().playSound(null, player.getBlockPos(), SoundEvents.ITEM_SHIELD_BREAK.value(), player.getSoundCategory());
        }
    }
}
