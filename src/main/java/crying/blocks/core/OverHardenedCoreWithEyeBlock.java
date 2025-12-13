package crying.blocks.core;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class OverHardenedCoreWithEyeBlock extends CoreBlock {    
    public OverHardenedCoreWithEyeBlock() {
        super(
            Properties.of()
            .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Crying.ID, "over-hardened_core_with_eye")))
            .destroyTime(-1F)
            .pushReaction(PushReaction.BLOCK)
            .explosionResistance((float) Integer.MAX_VALUE),
            "over-hardened_core_with_eye"
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register((itemGroup) -> itemGroup.addAfter(Crying.OVER_HARDENED_CORE, this.asItem()));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (placer instanceof ServerPlayer player)
            world.destroyBlock(pos, !player.isCreative());
        else
            world.destroyBlock(pos, true);
    }
}
