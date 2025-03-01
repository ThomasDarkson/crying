package crying.tools.blocks;

import org.jetbrains.annotations.Nullable;

import crying.tools.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.HeavyCoreBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OverHardenedCoreWithEye extends HeavyCoreBlock {    
    public OverHardenedCoreWithEye() {
        super(
            Settings.create()
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Crying.ID, "over-hardened_core_with_eye")))
            .hardness(-1F)
            .pistonBehavior(PistonBehavior.BLOCK)
            .resistance((float) Integer.MAX_VALUE)
        );

        create();
    }

    public void create() {
        Crying.registerBlock(this, "over-hardened_core_with_eye", true);
      
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> itemGroup.addAfter(Crying.overhardenedcore, this.asItem()));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (placer instanceof ServerPlayerEntity player)
            world.breakBlock(pos, !player.isCreative());
        else
            world.breakBlock(pos, true);
    }
}
