package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import crying.interfaces.FoodVars;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void render(GuiGraphics context, int mouseX, int mouseY, float deltaTicks, CallbackInfo info) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            FoodVars food = (FoodVars) (Object) client.player;
            String count = "" + food.getEatenCryingFoodCount() + " / " + Crying.MAX_CRYING_FOOD_COUNT;
            MutableComponent text = Component.literal(count);

            context.drawString(Minecraft.getInstance().font, text, 8, 12, ARGB.color(255, 255, 255), true);
        }
    }
}
