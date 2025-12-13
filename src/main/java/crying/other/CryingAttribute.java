package crying.other;

import crying.mixin.CryingAttributeFixer;
import java.util.Map;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class CryingAttribute {
    private final transient String id;
    private final transient RangedAttribute attribute;

    public static final Map<String, Double> attributes = Map.of(
        "attribute.name.armor", 999d,
        "attribute.name.armor_toughness", 999d,
        "attribute.name.knockback_resistance", 999d,
        "attribute.name.attack_damage", ((double) Integer.MAX_VALUE)
    );

    public double min;
    public double max;

    public CryingAttribute(String id, RangedAttribute attribute) {
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