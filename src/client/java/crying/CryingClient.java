package crying;

import crying.entities.crier.CrierEntityRenderer;
import crying.entities.lost_crier.LostCrierEntityRenderer;
import crying.other.CryingAttribute;
import crying.renderers.CriersHeartBlockEntityRenderer;
import crying.renderers.CryingFoodEntityRenderer;
import crying.renderers.GrapplingHookRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class CryingClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
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

        EntityRendererRegistry.register(Crying.CRIER, CrierEntityRenderer::new);
        EntityRendererRegistry.register(Crying.LOST_CRIER, LostCrierEntityRenderer::new);
        EntityRendererRegistry.register(Crying.GRANTER_ENTITY, ThrownItemRenderer::new);
        EntityRendererRegistry.register(Crying.EVIL_GRANTER_ENTITY, ThrownItemRenderer::new);
        EntityRendererRegistry.register(Crying.GRAPPLING_HOOK, GrapplingHookRenderer::new);

        BlockEntityRenderers.register(Crying.CRYING_FOOD_BLOCK_ENTITY, CryingFoodEntityRenderer::new);
        BlockEntityRenderers.register(Crying.CRIERS_HEART_BLOCK_ENTITY, CriersHeartBlockEntityRenderer::new);
    }

    public static Boolean compareHandtoArm(InteractionHand hand, HumanoidArm arm, HumanoidArm mainArm) {
        if (hand == InteractionHand.OFF_HAND) 
            return arm == (mainArm == HumanoidArm.LEFT ? HumanoidArm.RIGHT : HumanoidArm.LEFT);
        return arm == (mainArm == HumanoidArm.LEFT ? HumanoidArm.LEFT : HumanoidArm.RIGHT);
    }
}