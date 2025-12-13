package crying.blocks;

import crying.Crying;
import crying.interfaces.HasUniqueItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class VoidStainedCryingBlock extends Block implements HasUniqueItemSettings {
    public VoidStainedCryingBlock() {
        super(
            Properties.of().
            setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Crying.ID, "void-stained_crying_block"))).
            mapColor(MapColor.WARPED_HYPHAE).
            noLootTable().
            strength(-1, Integer.MAX_VALUE).
            lightLevel((state) -> {
                return 15;
            })
        );
        
        Crying.registerBlock(this, "void-stained_crying_block");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register((itemGroup) -> itemGroup.addAfter(Crying.CRYING_BLOCK, this.asItem()));
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        if (!entity.isSuppressingBounce()) {
            entity.causeFallDamage(fallDistance, 0.0F, world.damageSources().fall());
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
