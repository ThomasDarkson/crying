package crying.items;

import crying.Crying;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class GranterItem extends Item {
    public GranterItem() {
        this("granter");
    }

    public GranterItem(String id) {
        super(new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, id)))
            .fireproof()
            .maxCount(1)
            .rarity(Rarity.EPIC));

        Crying.register(this, id);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getBlockState(context.getBlockPos()).getBlock().getHardness() > 0) {
            context.getWorld().setBlockState(context.getBlockPos(), Crying.CRYING_BLOCK.getDefaultState());
            Crying.LOGGER.warn("Granter item used");
        }
        return ActionResult.PASS;
    }
}
