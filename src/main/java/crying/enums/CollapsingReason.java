package crying.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

public enum CollapsingReason implements StringIdentifiable {
    UNKNOWN("unknown"),
    HIGH_DAMAGE("high_damage"),
    BAD_FOOD("bad_food"),
    MURDER("murder"),
    LOW_LIGHT("low_light");

    private String name;
    private CollapsingReason(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public MutableText getTranslatableName() {
        return Text.translatable(asString());
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
            }
        return UNKNOWN;
    }
}
