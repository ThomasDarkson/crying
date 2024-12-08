package crying.tools.armors;

import java.util.Map;

import crying.tools.Crying;
import crying.tools.other.Crier;
import crying.tools.other.CryingTags;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;

public class CryingArmor implements ArmorMaterials {
    public static final ArmorMaterial CRYING_ARMOR_MATERIAL = Crying.registerMaterial(
        591,
        Map.of(
            EquipmentType.BOOTS, 6,
            EquipmentType.HELMET, 6,
            EquipmentType.CHESTPLATE, 16,
            EquipmentType.LEGGINGS, 12
        ),
        120,
        SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
        6f,
        0.2475f,
        CryingTags.CryingTag,
        CryingAssetKeys.CRYING
    );

    public static void checkAndApplyArmorEffect(ServerPlayerEntity player) {
        int amplifier = isWearingFullSet(player);
        if (amplifier <= 0)
        {
            if (player.hasStatusEffect(Crier.CRIER))
                player.removeStatusEffect(Crier.CRIER);

            Crier.setAmplifier((Crier) Crier.CRIER.value(), -1);
            return;
        }

        if (amplifier > 0) {
            Crier.setAmplifier((Crier) Crier.CRIER.value(), amplifier - 1);
            if (!player.hasStatusEffect(Crier.CRIER))
                player.addStatusEffect(new StatusEffectInstance(Crier.CRIER, -1, 0, false, false));
        }
    }

    public static int isWearingFullSet(ServerPlayerEntity player) {
        int amplifier = 0;
        ItemStack helmet = player.getInventory().getArmorStack(3); 
        ItemStack chestplate = player.getInventory().getArmorStack(2);
        ItemStack leggings = player.getInventory().getArmorStack(1);
        ItemStack boots = player.getInventory().getArmorStack(0);
        if (isCryingArmor(helmet))
            amplifier++;
        if (isCryingArmor(boots))
            amplifier++;
        if (isCryingArmor(leggings))
            amplifier++;
        if (isCryingArmor(chestplate))
            amplifier++;

        return amplifier;
    }

    public static boolean isCryingArmor(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem().toString().contains("crying");
    }
}
