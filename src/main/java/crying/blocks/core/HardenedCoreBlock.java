package crying.blocks.core;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class HardenedCoreBlock extends CoreBlock {
    public HardenedCoreBlock() {
        super(Settings.create()
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Crying.ID, "hardened_core")))
            .instrument(NoteBlockInstrument.SNARE)
            .sounds(BlockSoundGroup.HEAVY_CORE)
            .hardness(450.0F)
            .resistance(3200000F)
            .requiresTool(),
            "hardened_core");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> itemGroup.addAfter(Items.HEAVY_CORE, this.asItem()));
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
}
