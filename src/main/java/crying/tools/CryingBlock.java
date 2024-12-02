package crying.tools;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class CryingBlock {
    public Block block = null;

    float hardness = 400F;

    float resistance = 9600F;
    
    public CryingBlock() {
        create();
    }

    public void create() {
        block = CryingTools.registerBlock(new Block(AbstractBlock.Settings.create()
        .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(CryingTools.MOD_ID, "crying_block")))
        .strength(hardness, resistance)
        .pistonBehavior(PistonBehavior.NORMAL)
        .sounds(BlockSoundGroup.NETHERITE)
        .requiresTool()
        )
        , "crying_block", true);
      
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((itemGroup) -> itemGroup.add(block.asItem()));
    }
}
