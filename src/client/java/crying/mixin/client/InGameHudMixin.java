package crying.mixin.client;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import crying.interfaces.SanityManager;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    private static final Identifier ARMOR_EMPTY_TEXTURE = Identifier.of(Crying.ID, "hud/armor_empty");
    private static final Identifier ARMOR_HALF_TEXTURE = Identifier.of(Crying.ID, "hud/armor_half");
    private static final Identifier ARMOR_HALF_TEXTURE_2 = Identifier.of(Crying.ID, "hud/armor_half_2");
    private static final Identifier ARMOR_FULL_TEXTURE = Identifier.of(Crying.ID, "hud/armor_full");

    private static final Identifier EMPTY_TEXTURE = Identifier.of(Crying.ID, "hud/sanity_empty");
    private static final Identifier HALF_TEXTURE = Identifier.of(Crying.ID, "hud/sanity_half");
    private static final Identifier FULL_TEXTURE = Identifier.of(Crying.ID, "hud/sanity_full");
    private static final Identifier MAX_HALF_TEXTURE = Identifier.of(Crying.ID, "hud/sanity_half_max");
    private static final Identifier MAX_EMPTY_TEXTURE = Identifier.of(Crying.ID, "hud/sanity_half_empty");

    private static final Identifier INACTIVE_TEXTURE = Identifier.of(Crying.ID, "hud/sanity_inactive");

    @Inject(method = "renderArmor", at = @At("HEAD"))
    private static void renderArmor(DrawContext context, PlayerEntity player, int i, int j, int k, int x, CallbackInfo info) {
        int armor = player.getArmor();
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
                    o = context.getScaledWindowWidth() / 2 + 91 - (n - 20) * 8 - 9;
                    m = i - (j - 1) * k - 10;
                }
                if (n >= 30)
                {
                    o = context.getScaledWindowWidth() / 2 + 91 - (n - 30) * 8 - 9;
                    m = i - (j - 1) * k - 20;
                }
                if (n * 2 + 1 < armor) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ARMOR_FULL_TEXTURE, o, m, 9, 9);
                }
                if (n * 2 + 1 == armor) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, n >= 20 ? ARMOR_HALF_TEXTURE_2 : ARMOR_HALF_TEXTURE, o, m, 9, 9);
                }

                if (n * 2 + 1 > armor) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ARMOR_EMPTY_TEXTURE, o, m, 9, 9);
                }
            }
        }
    }

    @Inject(method = "renderFood", at = @At("HEAD"))
    private void renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo info) {
        SanityManager manager = SanityManager.getFromUUID(player.getUuidAsString());
        InGameHud hud = (InGameHud) (Object) this;
        if (manager == null) 
            manager = new SanityManager(player.getUuidAsString());
        if (!manager.isActive)
            return;

        int a = player.getMaxAir();
        int c = Math.clamp((long) player.getAir(), 0, a);
        boolean bl = player.isSubmergedIn(FluidTags.WATER) || c < a;
            
        float i = manager.getSanityLevel();
        float max = Math.round((float) manager.getMaxLevel() / 2F);
        
        int k = top - 10 - (bl ? 10 : 0);
        for(int j = 0; j < (Math.round(max)); ++j) {
            
            int l = right - j * 8 - 9;
            float dec = Crying.floorDecimal((float) manager.getMaxLevel() / 2F, 1);
            boolean ishalf = dec == 2.5F || dec == 7.5F;
            
            Identifier half = HALF_TEXTURE;
            Identifier empty = EMPTY_TEXTURE;
            Identifier full = FULL_TEXTURE;
            
            if (ishalf) {
                if (j == Math.round(max) - 1) {
                    full = MAX_HALF_TEXTURE;
                    half = MAX_HALF_TEXTURE;
                    empty = MAX_EMPTY_TEXTURE;
                }
            }

            if (manager.getCollapseRegenTicks() > 0) {
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, INACTIVE_TEXTURE, l, k, 9, 9);
            }
            else {
                if (j * 2F + 1F < i) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, full, l, k, 9, 9);
                }
                else if (j * 2F + 1F == i) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, half, l, k, 9, 9);
                }
                else {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, empty, l, k, 9, 9);
                }
            }
        }

        if (manager.getCollapseRegenTicks() > 0) {
            MutableText text = Text.translatable("sanity.collapsed");
            text.append(manager.getCollapsingReason().getTranslatableName().setStyle(Style.EMPTY.withColor(Formatting.RED)));

            MutableText text2 = Text.translatable("sanity.recovering");
            text2.append(Text.literal(Crying.tickToString(manager.getCollapseRegenTicks())).setStyle(Style.EMPTY.withColor(manager.getCollapseMultiplier() > 1 ? Formatting.AQUA : Formatting.WHITE)));

            int ll = (context.getScaledWindowWidth() - hud.getTextRenderer().getWidth(text)) / 2;
            context.drawTextWithBackground(hud.getTextRenderer(), text, ll, k - 30 - (bl ? -10 : 0), hud.getTextRenderer().getWidth(text), ColorHelper.getArgb(255, 255, 255));

            int ll2 = (context.getScaledWindowWidth() - hud.getTextRenderer().getWidth(text2)) / 2;
            context.drawTextWithBackground(hud.getTextRenderer(), text2, ll2, k - 20 - (bl ? -10 : 0), hud.getTextRenderer().getWidth(text2), ColorHelper.getArgb(255, 255, 255));
        }
    }
}