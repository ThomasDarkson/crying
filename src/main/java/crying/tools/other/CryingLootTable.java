package crying.tools.other;

import crying.tools.Crying;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class CryingLootTable {
    public static final RegistryKey<LootTable> CRYING_CAT_MORNING_GIFT = register("gameplay/crying_cat_morning_gift");

    private static RegistryKey<LootTable> register(String id) {
        return (RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(Crying.ID, id)));
    }
}
