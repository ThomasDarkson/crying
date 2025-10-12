package crying.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;

public enum CollapsingReason implements StringIdentifiable {
    UNKNOWN("unknown", Formatting.WHITE),
    HIGH_DAMAGE("high_damage", Formatting.RED),
    BAD_FOOD("bad_food", Formatting.DARK_GREEN),
    MURDER("murder", Formatting.DARK_RED),
    LOW_LIGHT("low_light", Formatting.GRAY),
    HYPOTHERMIA("hypothermia", Formatting.AQUA);

    private String name;
    private Formatting color;
    private CollapsingReason(String name, Formatting color) {
        this.name = name;
        this.color = color;
    }

    public Formatting getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public MutableText getTranslatableName() {
        return Text.translatable(asString()).setStyle(Style.EMPTY.withColor(this.getColor()));
    }

    @Override
    public String asString() {
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
