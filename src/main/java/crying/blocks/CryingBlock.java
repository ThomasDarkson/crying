package crying.blocks;

import crying.Crying;
import crying.interfaces.HasUniqueItemSettings;
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
import net.minecraft.world.level.material.MapColor;

public class CryingBlock extends Block implements HasUniqueItemSettings {    
    public CryingBlock() {
        super(
            Properties.of().
            setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Crying.ID, "crying_block"))).
            mapColor(MapColor.COLOR_BLACK).
            instrument(NoteBlockInstrument.BASEDRUM).
            requiresCorrectToolForDrops().
            strength(400.0F, 9600.0F).
            lightLevel((state) -> {
                return 10;
            })
        );
        
        Crying.registerBlock(this, "crying_block");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_BLOCK, this.asItem()));
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(1) == 0) {
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
