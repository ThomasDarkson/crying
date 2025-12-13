package crying.blocks.core;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class OverHardenedCoreBlock extends CoreBlock {    
    public OverHardenedCoreBlock() {
        super(
            Properties.of()
            .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Crying.ID, "over-hardened_core")))
            .mapColor(MapColor.METAL)
            .instrument(NoteBlockInstrument.SNARE)
            .sound(SoundType.HEAVY_CORE)
            .destroyTime(900.0F)
            .pushReaction(PushReaction.NORMAL)
            .explosionResistance(3200000F)
            .requiresCorrectToolForDrops(),
            "over-hardened_core"
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register((itemGroup) -> itemGroup.addAfter(Crying.HARDENED_CORE, this.asItem()));
    }
}
