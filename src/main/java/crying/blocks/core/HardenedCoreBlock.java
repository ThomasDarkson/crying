package crying.blocks.core;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class HardenedCoreBlock extends CoreBlock {
    public HardenedCoreBlock() {
        super(Properties.of()
            .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Crying.ID, "hardened_core")))
            .instrument(NoteBlockInstrument.SNARE)
            .sound(SoundType.HEAVY_CORE)
            .destroyTime(450.0F)
            .explosionResistance(3200000F)
            .requiresCorrectToolForDrops(),
            "hardened_core");

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register((itemGroup) -> itemGroup.addAfter(Items.HEAVY_CORE, this.asItem()));
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
}
