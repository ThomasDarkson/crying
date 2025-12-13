package crying.entities;

import crying.Crying;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EvilGranterEntity extends GranterEntity {
    public EvilGranterEntity(EntityType<? extends EvilGranterEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Crying.EVIL_GRANTER);
    }
}
