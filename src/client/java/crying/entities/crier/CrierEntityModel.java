package crying.entities.crier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

@Environment(EnvType.CLIENT)
public class CrierEntityModel<S extends CrierEntityRenderState> extends HumanoidModel<S> {
    public CrierEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);

        this.head.y = this.head.y - 2.0F + state.headFloatOffset * 8.0F;
        //this.head.yaw = (float) Math.toRadians(state.headSpinDegrees);

        this.body.yRot = 0.0F;

        this.leftArm.setRotation(0f, 0f, 0f);
    }
}
