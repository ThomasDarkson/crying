package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import semantic.ver.lib.SemanticVersion;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo info) {
        MutableText text = Text.literal("Crying Tools v");
        context.drawText(MinecraftClient.getInstance().textRenderer, text.append(Crying.VERSION.toString()), 2, 2, ColorHelper.getArgb(255, 255, 255), true);
        SemanticVersion ver = Crying.VERSION.newVersionAvailable();
        if (!ver.isInvalid()) {
            MutableText text2 = Text.translatable("newer.version.available");
            text2.append(ver.toString());

            context.drawText(MinecraftClient.getInstance().textRenderer, text2, 8, 12, ColorHelper.getArgb(255, 255, 255), true);
        }
    }
}
