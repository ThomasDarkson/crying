package crying.tools;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class CryingShieldItem extends Item {
    public static final int CRYING_SHIELD_HEALTH = 2688;

    public CryingShieldItem() {
        super(new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_shield")))
            .fireResistant()
            .rarity(Rarity.EPIC)
            .attributes(ItemAttributeModifiers.builder().add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Crying.ID, ""), 0.200000000000000618d, Operation.ADD_VALUE), EquipmentSlotGroup.HAND).build())
            .durability(CRYING_SHIELD_HEALTH));

        Crying.register(this, "crying_shield");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.SHIELD, this));
    }

    public void useShield(ItemStack stack, InteractionHand hand, Player player, Entity attacker, DamageSource source, int damage) {
        if (damage > 3)
            stack.hurtWithoutBreaking(damage, player);
        else
            stack.hurtWithoutBreaking(1, player);

        if (attacker != null && attacker instanceof LivingEntity entity && !entity.isDeadOrDying() && player.isShiftKeyDown()) {
            entity.hurtServer((ServerLevel) player.level(), new DamageSource(source.typeHolder(), player), Math.round(damage / 2));
        }

        player.level().playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK.value(), player.getSoundSource());
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.SHIELD_BREAK.value(), player.getSoundSource());
        }
    }
}
