package crying.tools;

import crying.tools.other.CryingAttribute;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;

public class CryingClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        String[] attributes = {"attribute.name.armor", "attribute.name.armor_toughness", "attribute.name.knockback_resistance"};
        for (String i : attributes)
        {
            switch (i) {
                case "attribute.name.armor":
                    CryingAttribute attribute = new CryingAttribute(i, (ClampedEntityAttribute) EntityAttributes.ARMOR.value());
                    attribute.fix();
                case "attribute.name.armor_toughness":
                    CryingAttribute attribute2 = new CryingAttribute(i, (ClampedEntityAttribute) EntityAttributes.ARMOR_TOUGHNESS.value());
                    attribute2.fix();
                case "attribute.name.knockback_resistance":
                    CryingAttribute attribute3 = new CryingAttribute(i, (ClampedEntityAttribute) EntityAttributes.KNOCKBACK_RESISTANCE.value());
                    attribute3.fix();
                default: break;
            }
        }
    }
}