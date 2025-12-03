package crying.entities;

import crying.Crying;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EvilGranterEntity extends GranterEntity {
    public EvilGranterEntity(EntityType<? extends EvilGranterEntity> type, World world) {
        super(type, world);
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(Crying.EVIL_GRANTER);
    }
}
