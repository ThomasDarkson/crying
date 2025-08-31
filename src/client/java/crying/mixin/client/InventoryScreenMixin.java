package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import crying.interfaces.FoodVars;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            FoodVars food = (FoodVars) (Object) client.player;
            String count = "" + food.getEatenCryingFoodCount() + " / " + Crying.MAX_CRYING_FOOD_COUNT;
            MutableText text = Text.literal(count);

            context.drawText(MinecraftClient.getInstance().textRenderer, text, 8, 12, ColorHelper.getArgb(255, 255, 255), true);
        }
    }
}
