package crying;

import crying.entities.crier.CrierEntityRenderer;
import crying.entities.lost_crier.LostCrierEntityRenderer;
import crying.other.CryingAttribute;
import crying.renderers.CriersHeartBlockEntityRenderer;
import crying.renderers.CryingFoodEntityRenderer;
import crying.renderers.GrapplingHookRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

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
        EntityRendererRegistry.register(Crying.LOST_CRIER, LostCrierEntityRenderer::new);
        EntityRendererRegistry.register(Crying.GRANTER_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Crying.EVIL_GRANTER_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Crying.GRAPPLING_HOOK, GrapplingHookRenderer::new);

        BlockEntityRendererFactories.register(Crying.CRYING_FOOD_BLOCK_ENTITY, CryingFoodEntityRenderer::new);
        BlockEntityRendererFactories.register(Crying.CRIERS_HEART_BLOCK_ENTITY, CriersHeartBlockEntityRenderer::new);
    }

    public static Boolean compareHandtoArm(Hand hand, Arm arm, Arm mainArm) {
        if (hand == Hand.OFF_HAND) 
            return arm == (mainArm == Arm.LEFT ? Arm.RIGHT : Arm.LEFT);
        return arm == (mainArm == Arm.LEFT ? Arm.LEFT : Arm.RIGHT);
    }
}