package crying.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import crying.interfaces.NbtInterface;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.TagValueInput;

@Mixin(TagValueInput.class)
public class NbtReadViewMixin implements NbtInterface {
    @Mutable
    @Final
    @Shadow
    private CompoundTag input;

    @Override
    public CompoundTag getNbt() {
        return this.input;
    }
}
