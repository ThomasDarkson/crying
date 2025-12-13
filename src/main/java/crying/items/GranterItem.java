package crying.items;

import crying.Crying;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;

public class GranterItem extends Item {
    public GranterItem() {
        this("granter");
    }

    public GranterItem(String id) {
        super(new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Crying.ID, id)))
            .fireResistant()
            .stacksTo(1)
            .rarity(Rarity.EPIC));

        Crying.register(this, id);
    }

    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos()).getBlock().defaultDestroyTime() > 0) {
            context.getLevel().setBlockAndUpdate(context.getClickedPos(), Crying.CRYING_BLOCK.defaultBlockState());
            Crying.LOGGER.warn("Granter item used");
        }
        return InteractionResult.PASS;
    }
}
