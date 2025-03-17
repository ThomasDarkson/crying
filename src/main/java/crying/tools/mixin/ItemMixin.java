package crying.tools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import crying.tools.enchantments.Feathered;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        int level = EnchantmentHelper.getLevel(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Feathered.FEATHERED), user.getMainHandStack());
        if (level > 0) {
            if (user != null) {
                if (user.isGliding()) {
                    double a = 1.25;
                    a += (double) (level / 4.5);

                    Vec3d vec3d = user.getRotationVector();
                    Vec3d vec3d2 = user.getVelocity();
                    user.setVelocity(vec3d2.add(vec3d.x * 0.1 + (vec3d.x * a - vec3d2.x) * 0.5, vec3d.y * 0.1 + (vec3d.y * a - vec3d2.y) * 0.5, vec3d.z * 0.1 + (vec3d.z * a - vec3d2.z) * 0.5));
                    info.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }
}
