package crying.tools.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.tools.Crying;
import crying.tools.interfaces.CryingManager;

@Mixin(InGameHud.class)
public class CryingArmorMixin {
    private static final Identifier ARMOR_EMPTY_TEXTURE = Identifier.of(Crying.MOD_ID, "hud/armor_empty");
    private static final Identifier ARMOR_HALF_TEXTURE = Identifier.of(Crying.MOD_ID, "hud/armor_half");
    private static final Identifier ARMOR_HALF_TEXTURE_2 = Identifier.of(Crying.MOD_ID, "hud/armor_half_2");
    private static final Identifier ARMOR_FULL_TEXTURE = Identifier.of(Crying.MOD_ID, "hud/armor_full");

    private static final Identifier EMPTY_TEXTURE = Identifier.of(Crying.MOD_ID, "hud/crying_empty");
    private static final Identifier HALF_TEXTURE = Identifier.of(Crying.MOD_ID, "hud/crying_half");
    private static final Identifier FULL_TEXTURE = Identifier.of(Crying.MOD_ID, "hud/crying_full");
    private static final Identifier MAX_HALF_TEXTURE = Identifier.of(Crying.MOD_ID, "hud/crying_half_max");
    private static final Identifier MAX_EMPTY_TEXTURE = Identifier.of(Crying.MOD_ID, "hud/crying_half_empty");

    private static final Identifier EMPTY_TEXTURE_PERMANENT = Identifier.of(Crying.MOD_ID, "hud/crying_empty_permanent");
    private static final Identifier HALF_TEXTURE_PERMANENT = Identifier.of(Crying.MOD_ID, "hud/crying_half_permanent");
    private static final Identifier FULL_TEXTURE_PERMANENT = Identifier.of(Crying.MOD_ID, "hud/crying_full_permanent");
    private static final Identifier MAX_HALF_TEXTURE_PERMANENT = Identifier.of(Crying.MOD_ID, "hud/crying_half_max_permanent");
    private static final Identifier MAX_EMPTY_TEXTURE_PERMANENT = Identifier.of(Crying.MOD_ID, "hud/crying_half_empty_permanent");

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
                    context.drawGuiTexture(RenderLayer::getGuiTextured, ARMOR_FULL_TEXTURE, o, m, 9, 9);
                }
                if (n * 2 + 1 == armor) {
                    context.drawGuiTexture(RenderLayer::getGuiTextured, n >= 20 ? ARMOR_HALF_TEXTURE_2 : ARMOR_HALF_TEXTURE, o, m, 9, 9);
                }

                if (n * 2 + 1 > armor) {
                    context.drawGuiTexture(RenderLayer::getGuiTextured, ARMOR_EMPTY_TEXTURE, o, m, 9, 9);
                }
            }
        }
    }

    @Inject(method = "renderFood", at = @At("HEAD"))
    private void renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo info) {
        CryingManager manager = CryingManager.getFromUUID(player.getUuidAsString());
        if (manager == null) 
            manager = new CryingManager(player.getUuidAsString());

        int a = player.getMaxAir();
        int c = Math.clamp((long) player.getAir(), 0, a);
        boolean bl = player.isSubmergedIn(FluidTags.WATER) || c < a;
            
        float i = manager.getCryingLevel();
        float max = Math.round((float) manager.getMaxLevel() / 2F);
    
        for(int j = 0; j < (Math.round(max)); ++j) {
            int k = top - 10 - (bl ? 10 : 0);
    
            int l = right - j * 8 - 9;
            float dec = Crying.floorDecimal((float) manager.getMaxLevel() / 2F, 1);
            boolean ishalf = dec == 2.5F || dec == 7.5F;
            
            Identifier half = HALF_TEXTURE;
            Identifier empty = EMPTY_TEXTURE;
            Identifier full = FULL_TEXTURE;

            if (j < (manager.getPermanentMaxLevel() / 2)) {
                half = HALF_TEXTURE_PERMANENT;
                empty = EMPTY_TEXTURE_PERMANENT;
                full = FULL_TEXTURE_PERMANENT;
            }
            
            if (ishalf) {
                if (j == Math.round(max) - 1) {
                    if (j < (manager.getPermanentMaxLevel() / 2)) {
                        full = MAX_HALF_TEXTURE_PERMANENT;
                        half = MAX_HALF_TEXTURE_PERMANENT;
                        empty = MAX_EMPTY_TEXTURE_PERMANENT;
                    }
                    else {
                        full = MAX_HALF_TEXTURE;
                        half = MAX_HALF_TEXTURE;
                        empty = MAX_EMPTY_TEXTURE;
                    }
                }
            }

            if (j * 2F + 1F < i) {
                context.drawGuiTexture(RenderLayer::getGuiTextured, full, l, k, 7, 9);
            }
            else if (j * 2F + 1F == i) {
                context.drawGuiTexture(RenderLayer::getGuiTextured, half, l, k, 7, 9);
            }
            else {
                context.drawGuiTexture(RenderLayer::getGuiTextured, empty, l, k, 7, 9);
            }
        }
    }
}