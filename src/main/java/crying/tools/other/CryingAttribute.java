package crying.tools.other;

import crying.tools.mixin.CryingAttributeFixer;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import java.util.Map;

public class CryingAttribute {
    private final transient String id;
    private final transient ClampedEntityAttribute attribute;

    public static final Map<String, Double> attributes = Map.of(
        "attribute.name.armor", 80d,
        "attribute.name.armor_toughness", 48d,
        "attribute.name.knockback_resistance", 10d
    );

    public double min;
    public double max;

    public CryingAttribute(String id, ClampedEntityAttribute attribute) {
        this.id = id;
        this.attribute = attribute;
        this.min = attribute.getMinValue();
        this.max = attributes.getOrDefault(id, attribute.getMaxValue());
    }

    public void fix() {
        if (attributes.containsKey(id) && attribute instanceof CryingAttributeFixer accessor) {
            accessor.attributeSetMin(min);
            if (max >= attribute.getMaxValue())
                accessor.attributeSetMax(max);
        }
    }
}