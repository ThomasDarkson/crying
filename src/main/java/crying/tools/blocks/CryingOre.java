package crying.tools.blocks;

import crying.tools.Crying;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
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
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class CryingOre extends CryingObsidianBlock 
{
    public static final RegistryKey<PlacedFeature> CRYING_ORE_KEY_LARGE = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of("crying", "crying_ore_large"));
    public static final RegistryKey<PlacedFeature> CRYING_ORE_KEY_SMALL = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of("crying", "crying_ore_small"));
    public static final RegistryKey<PlacedFeature> CRYING_ORE_KEY_MEDIUM = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of("crying", "crying_ore_medium"));

    public CryingOre() {
        super(
            Settings.create().
            registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Crying.MOD_ID, "crying_ore"))).
            mapColor(MapColor.BLACK).
            instrument(NoteBlockInstrument.BASEDRUM).
            requiresTool().
            strength(50.0F, 1200.0F).
            luminance((state) -> {
                return 10;
            })
        );
        create();
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
      if (random.nextInt(3) == 0) {
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
        Crying.registerBlock(this, "crying_ore", true);
      
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> itemGroup.addAfter(Items.ANCIENT_DEBRIS, this.asItem()));
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, CRYING_ORE_KEY_SMALL);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, CRYING_ORE_KEY_LARGE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, CRYING_ORE_KEY_MEDIUM);
    }
}
