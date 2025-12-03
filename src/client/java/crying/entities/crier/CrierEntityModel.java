package crying.entities.crier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;

@Environment(EnvType.CLIENT)
public class CrierEntityModel<S extends CrierEntityRenderState> extends BipedEntityModel<S> {
    public CrierEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public void setAngles(S state) {
        super.setAngles(state);

        this.head.originY = this.head.originY - 2.0F + state.headFloatOffset * 8.0F;
        //this.head.yaw = (float) Math.toRadians(state.headSpinDegrees);

        this.body.yaw = 0.0F;

        this.leftArm.setAngles(0f, 0f, 0f);
    }
}
