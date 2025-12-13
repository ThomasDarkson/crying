package crying.enums;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringRepresentable;

public enum CollapsingReason implements StringRepresentable {
    UNKNOWN("unknown", ChatFormatting.WHITE),
    HIGH_DAMAGE("high_damage", ChatFormatting.RED),
    BAD_FOOD("bad_food", ChatFormatting.DARK_GREEN),
    MURDER("murder", ChatFormatting.DARK_RED),
    LOW_LIGHT("low_light", ChatFormatting.GRAY),
    HYPOTHERMIA("hypothermia", ChatFormatting.AQUA);

    private String name;
    private ChatFormatting color;
    private CollapsingReason(String name, ChatFormatting color) {
        this.name = name;
        this.color = color;
    }

    public ChatFormatting getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public MutableComponent getTranslatableName() {
        return Component.translatable(getSerializedName()).setStyle(Style.EMPTY.withColor(this.getColor()));
    }

    @Override
    public String getSerializedName() {
        return "collapsing.reason." + this.name;
    }

    public static CollapsingReason fromString(String string) {
        if (string != null)
            switch (string.toLowerCase()) {
                case "high_damage":
                    return HIGH_DAMAGE;
                case "bad_food":
                    return BAD_FOOD;
                case "murder":
                    return MURDER;
                case "low_light":
                    return LOW_LIGHT;
                case "hypothermia":
                    return HYPOTHERMIA;
            }
        return UNKNOWN;
    }
}
