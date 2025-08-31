package crying.entities.lost_crier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;

@Environment(EnvType.CLIENT)
public class LostCrierEntityModel<S extends BipedEntityRenderState> extends BipedEntityModel<S> {
    public LostCrierEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    public void setAngles(S crierEntityRenderState) {
        super.setAngles(crierEntityRenderState);
    }
}
