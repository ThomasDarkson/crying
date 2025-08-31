package crying.blocks;

import java.util.Iterator;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import crying.entities.CrierEntity;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class HardCryingObsidianBlock extends Block {    
    @Nullable
    private BlockPattern crierPattern;

    public HardCryingObsidianBlock() {
        super(
            Settings.create().
            registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Crying.ID, "hard_crying_obsidian"))).
            mapColor(MapColor.BLACK).
            instrument(NoteBlockInstrument.BASEDRUM).
            requiresTool().
            strength(75.0F, 1320.0F).
            luminance((state) -> {
                return 10;
            })
        );

        Crying.registerBlock(this, "hard_crying_obsidian");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> itemGroup.addAfter(Items.CRYING_OBSIDIAN, this.asItem()));
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
                    world.addParticleClient(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, (double)pos.getX() + d, (double)pos.getY() + e, (double)pos.getZ() + f, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            this.trySpawnEntity(world, pos);
        }
    }

    private void trySpawnEntity(World world, BlockPos pos) {
        BlockPattern.Result result = this.getCrierPattern().searchAround(world, pos);
        if (result != null) {
            CrierEntity crierEntity = Crying.CRIER.spawn((ServerWorld) world, result.translate(0, 2, 0).getBlockPos(), SpawnReason.TRIGGERED);
            if (crierEntity != null) {
                spawnEntity(world, result, crierEntity, result.translate(0, 2, 0).getBlockPos());
            }
        } 
    }

    private static void spawnEntity(World world, BlockPattern.Result patternResult, Entity entity, BlockPos pos) {
        breakPatternBlocks(world, patternResult);
        entity.refreshPositionAndAngles((double)pos.getX() + 0.5, (double)pos.getY() + 0.05, (double)pos.getZ() + 0.5, 0.0F, 0.0F);
        Iterator<ServerPlayerEntity> var4 = world.getNonSpectatingEntities(ServerPlayerEntity.class, entity.getBoundingBox().expand(5.0)).iterator();

        while (var4.hasNext()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)var4.next();
            Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, entity);
        }

        updatePatternBlocks(world, patternResult);
    }

    public static void breakPatternBlocks(World world, BlockPattern.Result patternResult) {
        for (int i = 0; i < patternResult.getWidth(); ++i) {
            for (int j = 0; j < patternResult.getHeight(); ++j) {
                CachedBlockPosition cachedBlockPosition = patternResult.translate(i, j, 0);
                world.setBlockState(cachedBlockPosition.getBlockPos(), Blocks.AIR.getDefaultState(), 2);
                world.syncWorldEvent(2001, cachedBlockPosition.getBlockPos(), Block.getRawIdFromState(cachedBlockPosition.getBlockState()));
            }
        }

    }

    public static void updatePatternBlocks(World world, BlockPattern.Result patternResult) {
        for (int i = 0; i < patternResult.getWidth(); ++i) {
            for (int j = 0; j < patternResult.getHeight(); ++j) {
                CachedBlockPosition cachedBlockPosition = patternResult.translate(i, j, 0);
                world.updateNeighbors(cachedBlockPosition.getBlockPos(), Blocks.AIR);
            }
        }
    }

    private BlockPattern getCrierPattern() {
        if (this.crierPattern == null) {
                this.crierPattern = BlockPatternBuilder.start().aisle(new String[]{"~^~", "###", "?#?"}).where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Crying.HARD_CRYING_OBSIDIAN))).where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.CRYING_OBSIDIAN))).where('~', (pos) -> {
                return pos.getBlockState().isAir();
            }).where('?', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.CHAIN))).build();
        }
        return this.crierPattern;
    }
}
