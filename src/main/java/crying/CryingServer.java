package crying;

import crying.other.CryingAttribute;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class CryingServer implements DedicatedServerModInitializer
{
    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            if (server.isDedicatedServer()) {
                String[] attributes = {"attribute.name.armor", "attribute.name.armor_toughness", "attribute.name.knockback_resistance", "attribute.name.attack_damage"};
                for (String i : attributes)
                {
                    switch (i) {
                        case "attribute.name.armor":
                            CryingAttribute attribute = new CryingAttribute(i, (RangedAttribute) Attributes.ARMOR.value());
                            attribute.fix();
                        case "attribute.name.armor_toughness":
                            CryingAttribute attribute2 = new CryingAttribute(i, (RangedAttribute) Attributes.ARMOR_TOUGHNESS.value());
                            attribute2.fix();
                        case "attribute.name.knockback_resistance":
                            CryingAttribute attribute3 = new CryingAttribute(i, (RangedAttribute) Attributes.KNOCKBACK_RESISTANCE.value());
                            attribute3.fix();
                        case "attribute.name.attack_damage":
                            CryingAttribute attribute4 = new CryingAttribute(i, (RangedAttribute) Attributes.ATTACK_DAMAGE.value());
                            attribute4.fix();
                        default: break;
                    }
                }
            }
        });
    }
}
