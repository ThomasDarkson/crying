package crying.items;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class EyeConnectedToAStickItem extends Item {
    public EyeConnectedToAStickItem() {
        super(new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "eye_connected_to_a_stick")))
            .maxDamage(1236)
            .fireproof()
            .maxCount(1)
            .useCooldown(5F)
            .rarity(Rarity.EPIC));

        Crying.register(this, "eye_connected_to_a_stick");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> itemGroup.addAfter(Crying.EYE, this));
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world instanceof ServerWorld serverWorld) {
            try {
                ItemStack stack = user.getStackInHand(hand);
                stack.damage(1, user);
                boolean old = user.isInvulnerable();
                user.setInvulnerable(true);
                tryTeleport(serverWorld, user, user.getBlockPos(), hand);
                user.setInvulnerable(old);
                user.fallDistance = 0;
                return ActionResult.SUCCESS;
            } 
            catch (Exception e) {
                return ActionResult.FAIL;
            }
        }
        return ActionResult.PASS;
    }

    static void tryTeleport(ServerWorld world, PlayerEntity user, BlockPos pos, Hand hand) {
        RegistryEntry<DimensionType> entry = world.getDimensionEntry();
        RegistryKey<DimensionType> key = entry.getKey().orElse(Crying.CRYING_DIMENSION_TYPE);
        RegistryKey<World> teleportKey = Crying.CRYING_WORLD;
        if (key == Crying.CRYING_DIMENSION_TYPE) {
            teleportKey = World.OVERWORLD;
        }
        ServerWorld serverWorld = world.getServer().getWorld(teleportKey);
        BlockPos safePos = findSafeTeleportPosition(serverWorld, pos);
        user.teleportTo(new TeleportTarget(serverWorld, new Vec3d(safePos.getX(), safePos.getY(), safePos.getZ()), Vec3d.ZERO, 0, 0, TeleportTarget.NO_OP));
        user.swingHand(hand, user instanceof ServerPlayerEntity);
    }

    private static BlockPos findSafeTeleportPosition(ServerWorld world, BlockPos origin) {
        int maxHeight = world.getDimension().height();
        for (int radius = 0; radius < 618; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        BlockPos checkPos = origin.add(dx, dy, dz);

                        if (checkPos.getY() <= 2 || checkPos.getY() >= maxHeight - 2) {
                            continue;
                        }

                        BlockPos feetPos = checkPos;
                        BlockPos headPos = feetPos.up();
                        BlockPos belowPos = feetPos.down();

                        if (world.getBlockState(feetPos).isAir()
                                && world.getBlockState(headPos).isAir()
                                && world.getBlockState(belowPos).isSolidBlock(world, belowPos)) {
                            return feetPos;
                        }
                    }
                }
            }
        }

        return world.getSpawnPoint().getPos();
    }
}
