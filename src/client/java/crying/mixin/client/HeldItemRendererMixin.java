package crying.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.Crying;
import crying.tools.CryingShieldItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {    
    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V"), cancellable = true)
    private void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, int light, CallbackInfo info) {
        if (item.getItem() == Crying.CRIERS_SWORD) {
            HeldItemRenderer renderer = (HeldItemRenderer) (Object) this;
            matrices.push();

            float time = (player.age + tickProgress) / 8f;
            float sin = (float) Math.sin(time) * 0.05f;

            matrices.translate(0, sin, 0);

            renderer.renderItem(player, item, hand == Hand.MAIN_HAND ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, matrices, orderedRenderCommandQueue, light);

            matrices.pop();
            info.cancel();
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "HEAD"), cancellable = true)
    private void renderFirstPersonItemShield(AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, int light, CallbackInfo info) {
        if (item.getItem() instanceof CryingShieldItem) {
            info.cancel();
        }
    }
}
