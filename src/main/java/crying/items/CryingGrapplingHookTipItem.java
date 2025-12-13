package crying.items;

import crying.Crying;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CryingGrapplingHookTipItem extends Item {
    public CryingGrapplingHookTipItem() {
        super(new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_grappling_hook_tip")))
            .fireResistant()
            .rarity(Rarity.EPIC)
            .stacksTo(1));

        Crying.register(this, "crying_grappling_hook_tip");
    }
}
