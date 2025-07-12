package crying.tools.armors;

import java.util.Map;

import crying.tools.Crying;
import crying.tools.interfaces.SanityInterface;
import crying.tools.other.CryingTags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentType;
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
        50,
        SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
        5f,
        0.2f,
        CryingTags.CryingTag,
        CryingAssetKeys.CRYING
    );

    public static int setCount(LivingEntity entity) {
        int amplifier = 0;
        ItemStack h = entity.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack c = entity.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack l = entity.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack b = entity.getEquippedStack(EquipmentSlot.FEET);

        ItemStack armors[] = {h, c, l, b};

        for (ItemStack i : armors) {
            if (isCryingArmor(i)) {
                amplifier++;
            }
        }

        if (entity instanceof PlayerEntity player) {
            SanityInterface cryingPlayer = (SanityInterface) (Object) ((PlayerEntity) player);
            cryingPlayer.getManagerOverride_crying().adjustsanityLevel(amplifier);
        }
        return amplifier;
    }

    public static boolean isCryingArmor(ItemStack itemStack) {
        Item[] armors = {
            Crying.helmet,
            Crying.chestplate,
            Crying.leggings,
            Crying.boots
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
