package crying.items;

import crying.Crying;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class CryingGrapplingHookTipItem extends Item {
    public CryingGrapplingHookTipItem() {
        super(new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_grappling_hook_tip")))
            .fireproof()
            .rarity(Rarity.EPIC)
            .maxCount(1));

        Crying.register(this, "crying_grappling_hook_tip");
    }
}
