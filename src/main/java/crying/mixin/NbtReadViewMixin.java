package crying.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import crying.interfaces.NbtInterface;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.storage.NbtReadView;

@Mixin(NbtReadView.class)
public class NbtReadViewMixin implements NbtInterface {
    @Mutable
    @Final
    @Shadow
    private NbtCompound nbt;

    @Override
    public NbtCompound getNbt() {
        return this.nbt;
    }
}
