package crying.blocks;

import crying.Crying;
import crying.interfaces.HasUniqueItemSettings;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.MapColor;

public class CryingOreBlock extends Block implements HasUniqueItemSettings 
{
    public static final ResourceKey<PlacedFeature> CRYING_ORE_KEY_LARGE = ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Crying.ID, "crying_ore_large"));
    public static final ResourceKey<PlacedFeature> CRYING_ORE_KEY_SMALL = ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Crying.ID, "crying_ore_small"));
    public static final ResourceKey<PlacedFeature> CRYING_ORE_KEY_MEDIUM = ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Crying.ID, "crying_ore_medium"));
    public static final ResourceKey<PlacedFeature> CRYING_ORE_KEY_MEDIUM_OVERWORLD = ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Crying.ID, "crying_ore_medium_overworld"));

    public CryingOreBlock() {
        super(
            Properties.of().
            setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Crying.ID, "crying_ore"))).
            mapColor(MapColor.COLOR_BLACK).
            instrument(NoteBlockInstrument.BASEDRUM).
            requiresCorrectToolForDrops().
            strength(61.8F, 1200.0F).
            lightLevel((state) -> {
                return 10;
            })
        );
        Crying.registerBlock(this, "crying_ore");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register((itemGroup) -> itemGroup.addAfter(Items.ANCIENT_DEBRIS, this.asItem()));

        addFeatures();
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) == 0) {
            Direction direction = Direction.getRandom(random);
            if (direction != Direction.UP) {
                BlockPos blockPos = pos.relative(direction);
                BlockState blockState = world.getBlockState(blockPos);
                if (!state.canOcclude() || !blockState.isFaceSturdy(world, blockPos, direction.getOpposite())) {
                    double d = direction.getStepX() == 0 ? random.nextDouble() : 0.5 + (double)direction.getStepX() * 0.6;
                    double e = direction.getStepY() == 0 ? random.nextDouble() : 0.5 + (double)direction.getStepY() * 0.6;
                    double f = direction.getStepZ() == 0 ? random.nextDouble() : 0.5 + (double)direction.getStepZ() * 0.6;
                    world.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, (double)pos.getX() + d, (double)pos.getY() + e, (double)pos.getZ() + f, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    public void addFeatures() {
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Decoration.UNDERGROUND_ORES, CRYING_ORE_KEY_SMALL);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Decoration.UNDERGROUND_ORES, CRYING_ORE_KEY_LARGE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, CRYING_ORE_KEY_MEDIUM);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, CRYING_ORE_KEY_MEDIUM_OVERWORLD);
    }

    @Override
    public boolean isFireProof() {
        return true;
    }

    @Override
    public int getMaxCount() {
        return 64;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }
}
