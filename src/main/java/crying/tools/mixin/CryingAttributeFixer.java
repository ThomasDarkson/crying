package crying.tools.mixin;

import net.minecraft.entity.attribute.ClampedEntityAttribute;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClampedEntityAttribute.class)
public interface CryingAttributeFixer {
    @Accessor("minValue")
    @Mutable
    void attributeSetMin(double minValue);

    @Accessor("maxValue")
    @Mutable
    void attributeSetMax(double maxValue);
}