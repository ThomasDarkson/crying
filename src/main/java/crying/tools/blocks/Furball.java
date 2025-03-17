package crying.tools.blocks;

import net.minecraft.util.shape.VoxelShape;
import crying.tools.Crying;
import crying.tools.entities.CryingCatEntity;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Furball extends Block {
    private static final VoxelShape OUTLINE_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    
    public Furball() {
        super(
            Settings.create().
            registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Crying.ID, "furball"))).
            mapColor(MapColor.WHITE).
            strength(0F, 36000000F)
        );

        Crying.registerBlock(this, "furball", true);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((itemGroup) -> itemGroup.addAfter(Items.DRAGON_EGG, this.asItem()));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.getItem() == Crying.ingot) {
            if (stack.getCount() >= 4) {
                CryingCatEntity cat = new CryingCatEntity(Crying.CRYING_CAT, world);
                cat.setPos(pos.getX(), pos.getY(), pos.getZ());
                cat.setOgOwner(player);
                cat.setOwner(player);
                cat.setTamed(true, true);
                cat.setPersistent();
                world.spawnEntity(cat);
                world.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_CAT_AMBIENT, SoundCategory.AMBIENT);
                world.removeBlock(pos, false);

                stack.decrementUnlessCreative(4, player);
                return ActionResult.SUCCESS;
            }
            else {
                world.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_CAT_BEG_FOR_FOOD, SoundCategory.AMBIENT);
                return ActionResult.PASS;
            }
        }

        return ActionResult.PASS;
    }
}
