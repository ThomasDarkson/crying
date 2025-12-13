package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import semantic.ver.lib.SemanticVersion;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void render(GuiGraphics context, int mouseX, int mouseY, float deltaTicks, CallbackInfo info) {
        MutableComponent text = Component.literal("Crying Tools v");
        context.drawString(Minecraft.getInstance().font, text.append(Crying.VERSION.toString()), 2, 2, ARGB.color(255, 255, 255), true);
        SemanticVersion ver = Crying.VERSION.newVersionAvailable();
        if (!ver.isInvalid()) {
            MutableComponent text2 = Component.translatable("newer.version.available");
            text2.append(ver.toString());

            context.drawString(Minecraft.getInstance().font, text2, 8, 12, ARGB.color(255, 255, 255), true);
        }
    }
}
