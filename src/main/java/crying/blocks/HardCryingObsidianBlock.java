package crying.blocks;

import java.util.Iterator;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import crying.entities.CrierEntity;
import crying.interfaces.HasUniqueItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class HardCryingObsidianBlock extends Block implements HasUniqueItemSettings {    
    @Nullable
    private BlockPattern crierPattern;

    public HardCryingObsidianBlock() {
        super(
            Properties.of().
            setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Crying.ID, "hard_crying_obsidian"))).
            mapColor(MapColor.COLOR_BLACK).
            instrument(NoteBlockInstrument.BASEDRUM).
            requiresCorrectToolForDrops().
            strength(75.0F, 1320.0F).
            lightLevel((state) -> {
                return 10;
            })
        );

        Crying.registerBlock(this, "hard_crying_obsidian");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register((itemGroup) -> itemGroup.addAfter(Items.CRYING_OBSIDIAN, this.asItem()));
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(2) == 0) {
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
    protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.is(state.getBlock())) {
            this.trySpawnEntity(world, pos);
        }
    }

    private void trySpawnEntity(Level world, BlockPos pos) {
        BlockPattern.BlockPatternMatch result = this.getCrierPattern().find(world, pos);
        if (result != null) {
            CrierEntity crierEntity = Crying.CRIER.spawn((ServerLevel) world, result.getBlock(0, 2, 0).getPos(), EntitySpawnReason.TRIGGERED);
            if (crierEntity != null) {
                spawnEntity(world, result, crierEntity, result.getBlock(0, 2, 0).getPos());
            }
        } 
    }

    private static void spawnEntity(Level world, BlockPattern.BlockPatternMatch patternResult, Entity entity, BlockPos pos) {
        breakPatternBlocks(world, patternResult);
        entity.snapTo((double)pos.getX() + 0.5, (double)pos.getY() + 0.05, (double)pos.getZ() + 0.5, 0.0F, 0.0F);
        Iterator<ServerPlayer> var4 = world.getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(5.0)).iterator();

        while (var4.hasNext()) {
            ServerPlayer serverPlayerEntity = (ServerPlayer)var4.next();
            CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayerEntity, entity);
        }

        updatePatternBlocks(world, patternResult);
    }

    public static void breakPatternBlocks(Level world, BlockPattern.BlockPatternMatch patternResult) {
        for (int i = 0; i < patternResult.getWidth(); ++i) {
            for (int j = 0; j < patternResult.getHeight(); ++j) {
                BlockInWorld cachedBlockPosition = patternResult.getBlock(i, j, 0);
                world.setBlock(cachedBlockPosition.getPos(), Blocks.AIR.defaultBlockState(), 2);
                world.levelEvent(2001, cachedBlockPosition.getPos(), Block.getId(cachedBlockPosition.getState()));
            }
        }

    }

    public static void updatePatternBlocks(Level world, BlockPattern.BlockPatternMatch patternResult) {
        for (int i = 0; i < patternResult.getWidth(); ++i) {
            for (int j = 0; j < patternResult.getHeight(); ++j) {
                BlockInWorld cachedBlockPosition = patternResult.getBlock(i, j, 0);
                world.updateNeighborsAt(cachedBlockPosition.getPos(), Blocks.AIR);
            }
        }
    }

    private BlockPattern getCrierPattern() {
        if (this.crierPattern == null) {
                this.crierPattern = BlockPatternBuilder.start().aisle(new String[]{"~^~", "###", "?#?"}).where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Crying.HARD_CRYING_OBSIDIAN))).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.CRYING_OBSIDIAN))).where('~', (pos) -> {
                return pos.getState().isAir();
            }).where('?', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_CHAIN))).build();
        }
        return this.crierPattern;
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
