package crying.items;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

public class EyeConnectedToAStickItem extends Item {
    public EyeConnectedToAStickItem() {
        super(new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "eye_connected_to_a_stick")))
            .durability(1236)
            .fireResistant()
            .stacksTo(1)
            .useCooldown(5F)
            .rarity(Rarity.EPIC));

        Crying.register(this, "eye_connected_to_a_stick");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register((itemGroup) -> itemGroup.addAfter(Crying.EYE, this));
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        if (world instanceof ServerLevel serverWorld) {
            try {
                ItemStack stack = user.getItemInHand(hand);
                stack.hurtWithoutBreaking(1, user);
                boolean old = user.isInvulnerable();
                user.setInvulnerable(true);
                tryTeleport(serverWorld, user, user.blockPosition(), hand);
                user.setInvulnerable(old);
                user.fallDistance = 0;
                return InteractionResult.SUCCESS;
            } 
            catch (Exception e) {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }

    static void tryTeleport(ServerLevel world, Player user, BlockPos pos, InteractionHand hand) {
        Holder<DimensionType> entry = world.dimensionTypeRegistration();
        ResourceKey<DimensionType> key = entry.unwrapKey().orElse(Crying.CRYING_DIMENSION_TYPE);
        ResourceKey<Level> teleportKey = Crying.CRYING_WORLD;
        if (key == Crying.CRYING_DIMENSION_TYPE) {
            teleportKey = Level.OVERWORLD;
        }
        ServerLevel serverWorld = world.getServer().getLevel(teleportKey);
        BlockPos safePos = findSafeTeleportPosition(serverWorld, pos);
        user.teleport(new TeleportTransition(serverWorld, user, TeleportTransition.DO_NOTHING).withPosition(new Vec3(safePos.getX(), safePos.getY(), safePos.getZ())));
        user.swing(hand, user instanceof ServerPlayer);
    }

    private static BlockPos findSafeTeleportPosition(ServerLevel world, BlockPos origin) {
        final int maxRadius = 64;
        final int maxVerticalSearch = 16;

        int minY = world.getMinY() + 2;
        int maxY = world.getMaxY() - 2;

        BlockPos firstTry = findSafeOnColumn(world, origin.getX(), origin.getZ(), maxVerticalSearch, minY, maxY);
        if (firstTry != null) {
            return firstTry;
        }

        int originX = origin.getX();
        int originZ = origin.getZ();

        for (int radius = 1; radius <= maxRadius; radius++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int xEast = originX + radius;
                int xWest = originX - radius;
                int z = originZ + dz;

                BlockPos pos = findSafeOnColumn(world, xEast, z, maxVerticalSearch, minY, maxY);
                if (pos != null) return pos;

                pos = findSafeOnColumn(world, xWest, z, maxVerticalSearch, minY, maxY);
                if (pos != null) return pos;
            }

            for (int dx = -radius + 1; dx <= radius - 1; dx++) {
                int x = originX + dx;
                int zNorth = originZ - radius;
                int zSouth = originZ + radius;

                BlockPos pos = findSafeOnColumn(world, x, zNorth, maxVerticalSearch, minY, maxY);
                if (pos != null) return pos;

                pos = findSafeOnColumn(world, x, zSouth, maxVerticalSearch, minY, maxY);
                if (pos != null) return pos;
            }
        }

        return world.getSharedSpawnPos();
    }

    private static BlockPos findSafeOnColumn(ServerLevel world, int x, int z, int maxVerticalSearch, int minY, int maxY) {
        int topY = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

        if (topY < minY) 
            topY = minY;
        if (topY > maxY) 
            topY = maxY;

        for (int dy = 0; dy <= maxVerticalSearch; dy++) {
            int y = topY - dy;
            if (y < minY) break;

            BlockPos feetPos = new BlockPos(x, y, z);
            if (isSafeFeetPos(world, feetPos)) {
                return feetPos;
            }
        }

        for (int dy = 1; dy <= maxVerticalSearch; dy++) {
            int y = topY + dy;
            if (y > maxY) break;

            BlockPos feetPos = new BlockPos(x, y, z);
            if (isSafeFeetPos(world, feetPos)) {
                return feetPos;
            }
        }

        return null;
    }

    private static boolean isSafeFeetPos(ServerLevel world, BlockPos feetPos) {
        BlockPos headPos = feetPos.above();
        BlockPos belowPos = feetPos.below();

        return world.getBlockState(feetPos).isAir()
            && world.getBlockState(headPos).isAir()
            && world.getBlockState(belowPos).isRedstoneConductor(world, belowPos);
    }
}
