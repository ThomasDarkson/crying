package crying.interfaces;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

public interface OxidizableCryingTool extends CryingTool {
    default WeatherState getOxidationLevel(ItemStack stack) {
        if (stack.has(Crying.OXIDATION_LEVEL)) {
            switch (stack.getOrDefault(Crying.OXIDATION_LEVEL, "unaffected").toLowerCase()) {
                case "exposed":
                    return WeatherState.EXPOSED;
                case "weathered":
                    return WeatherState.WEATHERED;
                case "oxidized":
                    return WeatherState.OXIDIZED;
            }
        }

        return WeatherState.UNAFFECTED;
    }

    @Nullable
    default MutableComponent getOxidizedName(ItemStack stack) {
        MutableComponent text = Component.literal("");
        boolean shouldReturn = false;
        boolean wasWaxed = false;
        if (stack.getOrDefault(Crying.WAS_WAXED, false)) {
            text.append(Component.translatable("crying.waxed"));
            shouldReturn = true;
            wasWaxed = true;
        }

        WeatherState level = getOxidationLevel(stack);
        switch (level) {
            case WeatherState.EXPOSED:
                if (wasWaxed)
                    text.append(Component.literal(" "));
                text.append(Component.translatable("crying.exposed"));
                break;
            case WeatherState.WEATHERED:
                if (wasWaxed)
                    text.append(Component.literal(" "));
                text.append(Component.translatable("crying.weathered"));
                break;
            case WeatherState.OXIDIZED:
                if (wasWaxed)
                    text.append(Component.literal(" "));
                text.append(Component.translatable("crying.oxidized"));
                break;
            default:
                return shouldReturn ? text : null;
        }

        return text;
    }

    default void tickInventory(ItemStack stack, ServerLevel world, Entity entity, EquipmentSlot slot) {
        boolean holding = false;
        int ageToCheck = (int) (20 * (world.tickRateManager().tickrate() / 20F));
        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ageToCheck = (int) (1200 * (world.tickRateManager().tickrate() / 20F));
            holding = true;
        }

        if (entity.tickCount % ageToCheck == 0) {
            int seconds = stack.getOrDefault(Crying.OXIDATION_SECONDS, 0);
            stack.set(Crying.OXIDATION_SECONDS, seconds + (holding ? 60 : 1));
            stack.set(Crying.OXIDATION_LEVEL, checkSeconds(stack).getSerializedName());
        }
    }

    private static WeatherState checkSeconds(ItemStack stack) {
        int value = stack.getOrDefault(Crying.OXIDATION_SECONDS, 0);
        int markiplier = 1;
        if (stack.getOrDefault(Crying.WAS_WAXED, false))
            markiplier = 2;

        if (value >= 72000 * markiplier) 
            return WeatherState.OXIDIZED;
        else if (value >= 48000 * markiplier)
            return WeatherState.WEATHERED;
        else if (value >= 24000 * markiplier)
            return WeatherState.EXPOSED;

        return WeatherState.UNAFFECTED;
    }
}
