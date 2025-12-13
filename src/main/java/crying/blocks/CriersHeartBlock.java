package crying.blocks;

import com.mojang.serialization.MapCodec;

import crying.Crying;
import crying.entities.CriersHeartBlockEntity;
import crying.interfaces.HasUniqueItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CriersHeartBlock extends BaseEntityBlock implements HasUniqueItemSettings {
    static final VoxelShape SHAPE = Shapes.or(
		Shapes.box(0.4375, 0, 0.4375, 0.5, 0.0625, 0.5),
		Shapes.box(0.375, 0.0625, 0.4375, 0.5625, 0.125, 0.5),
		Shapes.box(0.3125, 0.125, 0.4375, 0.625, 0.1875, 0.5),
		Shapes.box(0.25, 0.1875, 0.4375, 0.6875, 0.25, 0.5),
		Shapes.box(0.1875, 0.25, 0.4375, 0.75, 0.3125, 0.5),
		Shapes.box(0.5, 0.3125, 0.4375, 0.75, 0.375, 0.5),
		Shapes.box(0.1875, 0.3125, 0.4375, 0.4375, 0.375, 0.5),
		Shapes.box(0.4375, 0.3125, 0.4375, 0.5, 0.375, 0.5),
		Shapes.box(0.25, 0.375, 0.4375, 0.4375, 0.4375, 0.5),
		Shapes.box(0.5, 0.375, 0.4375, 0.6875, 0.4375, 0.5),
		Shapes.box(0.5625, 0.4375, 0.4375, 0.625, 0.5, 0.5),
		Shapes.box(0.3125, 0.4375, 0.4375, 0.375, 0.5, 0.5),
		Shapes.box(0.40625, 0.375, 0.44375, 0.45625, 0.5625, 0.49375),
		Shapes.box(0.40625, 0.4375, 0.4375, 0.45625, 0.625, 0.4875),
		Shapes.box(0.5, 0.4375, 0.4375, 0.5625, 0.625, 0.5),
		Shapes.box(0.3125, 0.125, 0.375, 0.625, 0.375, 0.4375),
		Shapes.box(0.3125, 0.125, 0.5, 0.625, 0.375, 0.5625)
	);

    public CriersHeartBlock(Properties settings) {
        super(settings);

        Crying.registerBlock(this, "criers_heart");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((itemGroup) -> itemGroup.accept(this.asItem()));
    }
    
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, Crying.CRIERS_HEART_BLOCK_ENTITY, CriersHeartBlockEntity::ticker);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos arg0, BlockState arg1) {
        return new CriersHeartBlockEntity(arg0, arg1);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CriersHeartBlock::new);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean isFireProof() {
        return true;
    }

    @Override
    public int getMaxCount() {
        return 1;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }
}
