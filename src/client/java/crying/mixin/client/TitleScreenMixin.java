package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import crying.backend.CryingVersion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo info) {
        context.drawText(MinecraftClient.getInstance().textRenderer, Crying.VERSION.toDisplayText(), 2, 2, ColorHelper.getArgb(255, 255, 255), true);
        CryingVersion ver = Crying.CHECKER.getLatestVersion();
        if (ver != null) {
            MutableText text = Text.translatable("newer.version.available");
            text.append(ver.toText());

            context.drawText(MinecraftClient.getInstance().textRenderer, text, 8, 12, ColorHelper.getArgb(255, 255, 255), true);
        }
    }
}
