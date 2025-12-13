package crying.interfaces;

import net.minecraft.world.item.Rarity;

public interface HasUniqueItemSettings {
    boolean isFireProof();
    int getMaxCount();
    Rarity getRarity();
}
