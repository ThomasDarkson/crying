package crying.entities.lost_crier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

@Environment(EnvType.CLIENT)
public class LostCrierEntityModel<S extends HumanoidRenderState> extends HumanoidModel<S> {
    public LostCrierEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public void setupAnim(S crierEntityRenderState) {
        super.setupAnim(crierEntityRenderState);
    }
}
