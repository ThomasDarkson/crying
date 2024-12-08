package crying.tools.blocks;

import crying.tools.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class HardCryingObsidian extends CryingObsidianBlock {    
    public HardCryingObsidian() {
        super(
            Settings.create().
            registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Crying.MOD_ID, "hard_crying_obsidian"))).
            mapColor(MapColor.BLACK).
            instrument(NoteBlockInstrument.BASEDRUM).
            requiresTool().
            strength(54.0F, 1280.0F).
            luminance((state) -> {
                return 10;
            })
        );
        create();
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
      if (random.nextInt(2) == 0) {
         Direction direction = Direction.random(random);
         if (direction != Direction.UP) {
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            if (!state.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
               double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5 + (double)direction.getOffsetX() * 0.6;
               double e = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5 + (double)direction.getOffsetY() * 0.6;
               double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5 + (double)direction.getOffsetZ() * 0.6;
               world.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, (double)pos.getX() + d, (double)pos.getY() + e, (double)pos.getZ() + f, 0.0, 0.0, 0.0);
            }
         }
      }
   }

    public void create() {
        Crying.registerBlock(this, "hard_crying_obsidian", true);
      
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> itemGroup.addAfter(Items.CRYING_OBSIDIAN, this.asItem()));
    }
}
