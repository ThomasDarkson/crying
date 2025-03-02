package crying.tools;

import crying.tools.entities.CrierEntityRenderer;
import crying.tools.other.CryingAttribute;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;

public class CryingClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        String[] attributes = {"attribute.name.armor", "attribute.name.armor_toughness", "attribute.name.knockback_resistance", "attribute.name.attack_damage"};
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
                case "attribute.name.attack_damage":
                    CryingAttribute attribute4 = new CryingAttribute(i, (ClampedEntityAttribute) EntityAttributes.ATTACK_DAMAGE.value());
                    attribute4.fix();
                default: break;
            }
        }

        EntityRendererRegistry.register(Crying.CRIER, CrierEntityRenderer::new);
    }
}