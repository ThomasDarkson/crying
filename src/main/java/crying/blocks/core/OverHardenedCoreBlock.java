package crying.blocks.core;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class OverHardenedCoreBlock extends CoreBlock {    
    public OverHardenedCoreBlock() {
        super(
            Settings.create()
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Crying.ID, "over-hardened_core")))
            .mapColor(MapColor.IRON_GRAY)
            .instrument(NoteBlockInstrument.SNARE)
            .sounds(BlockSoundGroup.HEAVY_CORE)
            .hardness(900.0F)
            .pistonBehavior(PistonBehavior.NORMAL)
            .resistance(3200000F)
            .requiresTool(),
            "over-hardened_core"
        );
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> itemGroup.addAfter(Crying.HARDENED_CORE, this.asItem()));
    }
}
