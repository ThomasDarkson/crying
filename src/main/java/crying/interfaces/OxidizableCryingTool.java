package crying.interfaces;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import net.minecraft.block.Oxidizable.OxidationLevel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public interface OxidizableCryingTool extends CryingTool {
    default OxidationLevel getOxidationLevel(ItemStack stack) {
        if (stack.contains(Crying.OXIDATION_LEVEL)) {
            switch (stack.getOrDefault(Crying.OXIDATION_LEVEL, "unaffected").toLowerCase()) {
                case "exposed":
                    return OxidationLevel.EXPOSED;
                case "weathered":
                    return OxidationLevel.WEATHERED;
                case "oxidized":
                    return OxidationLevel.OXIDIZED;
            }
        }

        return OxidationLevel.UNAFFECTED;
    }

    @Nullable
    default MutableText getOxidizedName(ItemStack stack) {
        MutableText text = Text.literal("");
        boolean shouldReturn = false;
        boolean wasWaxed = false;
        if (stack.getOrDefault(Crying.WAS_WAXED, false)) {
            text.append(Text.translatable("crying.waxed"));
            shouldReturn = true;
            wasWaxed = true;
        }

        OxidationLevel level = getOxidationLevel(stack);
        switch (level) {
            case OxidationLevel.EXPOSED:
                if (wasWaxed)
                    text.append(Text.literal(" "));
                text.append(Text.translatable("crying.exposed"));
                break;
            case OxidationLevel.WEATHERED:
                if (wasWaxed)
                    text.append(Text.literal(" "));
                text.append(Text.translatable("crying.weathered"));
                break;
            case OxidationLevel.OXIDIZED:
                if (wasWaxed)
                    text.append(Text.literal(" "));
                text.append(Text.translatable("crying.oxidized"));
                break;
            default:
                return shouldReturn ? text : null;
        }

        return text;
    }

    default void tickInventory(ItemStack stack, ServerWorld world, Entity entity, EquipmentSlot slot) {
        boolean holding = false;
        int ageToCheck = (int) (20 * (world.getTickManager().getTickRate() / 20F));
        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ageToCheck = (int) (1200 * (world.getTickManager().getTickRate() / 20F));
            holding = true;
        }

        if (entity.age % ageToCheck == 0) {
            int seconds = stack.getOrDefault(Crying.OXIDATION_SECONDS, 0);
            stack.set(Crying.OXIDATION_SECONDS, seconds + (holding ? 60 : 1));
            stack.set(Crying.OXIDATION_LEVEL, checkSeconds(stack).asString());
        }
    }

    private static OxidationLevel checkSeconds(ItemStack stack) {
        int value = stack.getOrDefault(Crying.OXIDATION_SECONDS, 0);
        int markiplier = 1;
        if (stack.getOrDefault(Crying.WAS_WAXED, false))
            markiplier = 2;

        if (value >= 72000 * markiplier) 
            return OxidationLevel.OXIDIZED;
        else if (value >= 48000 * markiplier)
            return OxidationLevel.WEATHERED;
        else if (value >= 24000 * markiplier)
            return OxidationLevel.EXPOSED;

        return OxidationLevel.UNAFFECTED;
    }
}
