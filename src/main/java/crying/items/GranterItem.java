package crying.items;

import crying.Crying;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class GranterItem extends Item {
    public GranterItem() {
        super(new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "granter")))
            .fireproof()
            .maxCount(1)
            .rarity(Rarity.EPIC));

        Crying.register(this, "granter");
    }
}
