package crying.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;

@Environment(EnvType.CLIENT)
public class CrierEntityModel<S extends BipedEntityRenderState> extends BipedEntityModel<S> {
    public CrierEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    public void setAngles(S crierEntityRenderState) {
        super.setAngles(crierEntityRenderState);
    }
}
