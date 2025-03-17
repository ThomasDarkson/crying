package crying.tools.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crying.tools.entities.CryingCatEntity;
import crying.tools.interfaces.PrivateCatFieldsInterface;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.DyeColor;

@Mixin(CatEntity.class)
public class CatEntityMixin implements PrivateCatFieldsInterface {
    @Final
	@Mutable
	@Shadow
	private static TrackedData<Boolean> HEAD_DOWN;

    @Final
	@Mutable
	@Shadow
    private static TrackedData<Integer> COLLAR_COLOR;

    @Override
    public void set_HeadDown(boolean down) {
        CatEntity cat = (CatEntity) (Object) this;
        cat.getDataTracker().set(HEAD_DOWN, down);
    }

    @Override
    public boolean get_HeadDown() {
        CatEntity cat = (CatEntity) (Object) this;
        return cat.getDataTracker().get(HEAD_DOWN);
    }

    @Inject(method = "setCollarColor", at = @At("HEAD"))
    private void setCollarColor(DyeColor color, CallbackInfo info) {
        if (((Object) this) instanceof CryingCatEntity) {
            info.cancel();
        } 
    }
}
