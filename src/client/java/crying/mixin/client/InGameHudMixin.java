package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import crying.interfaces.SanityManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;

@Mixin(Gui.class)
public class InGameHudMixin {
    private static final ResourceLocation ARMOR_EMPTY_TEXTURE = ResourceLocation.fromNamespaceAndPath(Crying.ID, "hud/armor_empty");
    private static final ResourceLocation ARMOR_HALF_TEXTURE = ResourceLocation.fromNamespaceAndPath(Crying.ID, "hud/armor_half");
    private static final ResourceLocation ARMOR_HALF_TEXTURE_2 = ResourceLocation.fromNamespaceAndPath(Crying.ID, "hud/armor_half_2");
    private static final ResourceLocation ARMOR_FULL_TEXTURE = ResourceLocation.fromNamespaceAndPath(Crying.ID, "hud/armor_full");

    private static final ResourceLocation EMPTY_TEXTURE = ResourceLocation.fromNamespaceAndPath(Crying.ID, "hud/sanity_empty");
    private static final ResourceLocation HALF_TEXTURE = ResourceLocation.fromNamespaceAndPath(Crying.ID, "hud/sanity_half");
    private static final ResourceLocation FULL_TEXTURE = ResourceLocation.fromNamespaceAndPath(Crying.ID, "hud/sanity_full");
    private static final ResourceLocation MAX_HALF_TEXTURE = ResourceLocation.fromNamespaceAndPath(Crying.ID, "hud/sanity_half_max");
    private static final ResourceLocation MAX_EMPTY_TEXTURE = ResourceLocation.fromNamespaceAndPath(Crying.ID, "hud/sanity_half_empty");

    private static final ResourceLocation INACTIVE_TEXTURE = ResourceLocation.fromNamespaceAndPath(Crying.ID, "hud/sanity_inactive");

    @Inject(method = "renderArmor", at = @At("HEAD"))
    private static void renderArmor(GuiGraphics context, Player player, int i, int j, int k, int x, CallbackInfo info) {
        int armor = player.getArmorValue();
        if (armor > 20) {
            var max = 20;
            if (armor > 40)
                max = 30;
            if (armor > 60)
                max = 40;

            int m = i - (j - 1) * k - 20;

            for (int n = 10; n < max; ++n) {
                int o = x + (n - 10) * 8;
                if (n >= 20)
                {
                    o = context.guiWidth() / 2 + 91 - (n - 20) * 8 - 9;
                    m = i - (j - 1) * k - 10;
                }
                if (n >= 30)
                {
                    o = context.guiWidth() / 2 + 91 - (n - 30) * 8 - 9;
                    m = i - (j - 1) * k - 20;
                }
                if (n * 2 + 1 < armor) {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_FULL_TEXTURE, o, m, 9, 9);
                }
                if (n * 2 + 1 == armor) {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, n >= 20 ? ARMOR_HALF_TEXTURE_2 : ARMOR_HALF_TEXTURE, o, m, 9, 9);
                }

                if (n * 2 + 1 > armor) {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_EMPTY_TEXTURE, o, m, 9, 9);
                }
            }
        }
    }

    @Inject(method = "renderFood", at = @At("HEAD"))
    private void renderFood(GuiGraphics context, Player player, int top, int right, CallbackInfo info) {
        SanityManager manager = SanityManager.getFromUUID(player.getStringUUID());
        Gui hud = (Gui) (Object) this;
        if (manager == null) 
            manager = new SanityManager(player.getStringUUID());
        if (!manager.isActive)
            return;

        int a = player.getMaxAirSupply();
        int c = Math.clamp((long) player.getAirSupply(), 0, a);
        boolean bl = player.isEyeInFluid(FluidTags.WATER) || c < a;
            
        float i = manager.getSanityLevel();
        float max = Math.round((float) manager.getMaxLevel() / 2F);
        
        int k = top - 10 - (bl ? 10 : 0);
        for(int j = 0; j < (Math.round(max)); ++j) {
            
            int l = right - j * 8 - 9;
            float dec = Crying.floorDecimal((float) manager.getMaxLevel() / 2F, 1);
            boolean ishalf = dec == 2.5F || dec == 7.5F;
            
            ResourceLocation half = HALF_TEXTURE;
            ResourceLocation empty = EMPTY_TEXTURE;
            ResourceLocation full = FULL_TEXTURE;
            
            if (ishalf) {
                if (j == Math.round(max) - 1) {
                    full = MAX_HALF_TEXTURE;
                    half = MAX_HALF_TEXTURE;
                    empty = MAX_EMPTY_TEXTURE;
                }
            }

            if (manager.getCollapseRegenTicks() > 0) {
                context.blitSprite(RenderPipelines.GUI_TEXTURED, INACTIVE_TEXTURE, l, k, 9, 9);
            }
            else {
                if (j * 2F + 1F < i) {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, full, l, k, 9, 9);
                }
                else if (j * 2F + 1F == i) {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, half, l, k, 9, 9);
                }
                else {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, empty, l, k, 9, 9);
                }
            }
        }

        if (manager.getCollapseRegenTicks() > 0 && manager.getMaxLevel() > 0) {
            MutableComponent text = Component.translatable("sanity.collapsed");
            text.append(manager.getCollapsingReason().getTranslatableName());

            MutableComponent text2 = Component.translatable("sanity.recovering");
            text2.append(Component.literal(Crying.tickToString(manager.getCollapseRegenTicks())).setStyle(Style.EMPTY.withColor(manager.getCollapseMultiplier() > 1 ? ChatFormatting.AQUA : ChatFormatting.WHITE)));

            int ll = (context.guiWidth() - hud.getFont().width(text)) / 2;
            context.drawStringWithBackdrop(hud.getFont(), text, ll, k - 30 - (bl ? -10 : 0), hud.getFont().width(text), ARGB.color(255, 255, 255));

            int ll2 = (context.guiWidth() - hud.getFont().width(text2)) / 2;
            context.drawStringWithBackdrop(hud.getFont(), text2, ll2, k - 20 - (bl ? -10 : 0), hud.getFont().width(text2), ARGB.color(255, 255, 255));
        }
    }
}