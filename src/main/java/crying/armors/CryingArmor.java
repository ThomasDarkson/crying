package crying.armors;

import java.util.Map;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import crying.Crying;
import crying.other.CryingTags;

public class CryingArmor implements ArmorMaterials {
    public static final ArmorMaterial CRYING_ARMOR_MATERIAL = Crying.registerMaterial(
        591,
        Map.of(
            ArmorType.BODY, 20,
            ArmorType.BOOTS, 6,
            ArmorType.HELMET, 6,
            ArmorType.CHESTPLATE, 16,
            ArmorType.LEGGINGS, 12
        ),
        50,
        SoundEvents.ARMOR_EQUIP_NETHERITE,
        5f,
        0.2f,
        CryingTags.CryingTag,
        CryingAssetKeys.CRYING
    );

    public static final ArmorMaterial CRYING_WITH_ELYTRA_ARMOR_MATERIAL = Crying.registerMaterial(
        591,
        Map.of(
            ArmorType.BODY, 20,
            ArmorType.BOOTS, 6,
            ArmorType.HELMET, 6,
            ArmorType.CHESTPLATE, 16,
            ArmorType.LEGGINGS, 12
        ),
        50,
        SoundEvents.ARMOR_EQUIP_NETHERITE,
        5f,
        0.2f,
        CryingTags.CryingTag,
        CryingAssetKeys.CRYING_WITH_ELYTRA
    );

    public static int cryingArmorCount(LivingEntity entity) {
        int amplifier = 0;
        ItemStack h = entity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack c = entity.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack l = entity.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack b = entity.getItemBySlot(EquipmentSlot.FEET);

        ItemStack armors[] = {h, c, l, b};

        for (ItemStack i : armors) {
            if (isCryingArmor(i)) {
                amplifier++;
            }
        }

        return amplifier;
    }

    public static int setCount(LivingEntity entity) {
        int count = cryingArmorCount(entity);

        if (entity instanceof Player player) {
            Crying.getSanityManager(player).adjustSanityLevel(count);
        }
        return count;
    }

    public static boolean isCryingArmor(ItemStack itemStack) {
        Item[] armors = {
            Crying.CRYING_HELMET,
            Crying.CRYING_CHESTPLATE,
            Crying.CRYING_CHESTPLATE_WITH_ELYTRA,
            Crying.CRYING_LEGGINGS,
            Crying.CRYING_BOOTS,
        };
        if (itemStack != null) {
            boolean returner = false;
            Item armor = itemStack.getItem();
            for (Item i : armors) {
                if (armor == i) {
                    returner = true;
                    break;
                }
            }

            return returner;
        }
        return false;
    }
}
