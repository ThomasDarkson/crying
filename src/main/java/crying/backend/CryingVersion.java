package crying.backend;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

public class CryingVersion {
    public final int major;
    public final int minor;
    public final int patch;

    private static int majorColor = ColorHelper.getArgb(255, 255, 255, 255);
    private static int minorColor = ColorHelper.getArgb(255, 255, 255, 255);
    private static int patchColor = ColorHelper.getArgb(255, 255, 255, 255);

    private static boolean OFFICIAL = true; // Make this false if forked

    private CryingVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public MutableText toDisplayText() {
        MutableText text = Text.empty();
        text.append("Crying Tools ").append(toText());
        return text;
    }

    public MutableText toText() {
        MutableText text = Text.empty();
        text.append(Text.literal("v" + major).setStyle(Style.EMPTY.withColor(majorColor)));
        text.append(Text.literal("." + minor).setStyle(Style.EMPTY.withColor(minorColor)));
        text.append(Text.literal("." + patch).setStyle(Style.EMPTY.withColor(patchColor)));
        if (!OFFICIAL) {
            text.append(Text.literal(" (Unofficial)"));
        }
        return text;
    }

    public String toString() {
        String s = major + "." + minor + "." + patch;
        if (!OFFICIAL) {
            s += " (Unofficial)";
        }
        return s;
    }

    public static CryingVersion version(int major, int minor, int patch) {
        return new CryingVersion(major, minor, patch);
    }
}
