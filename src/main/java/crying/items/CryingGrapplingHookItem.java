package crying.items;

import crying.Crying;
import crying.entities.GrapplingHookEntity;
import crying.interfaces.HookVars;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CryingGrapplingHookItem extends Item {
    public CryingGrapplingHookItem() {
        super(new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_grappling_hook")))
            .component(Crying.THROWN, false)
            .component(Crying.HOOK_UUID, "")
            .rarity(Rarity.EPIC)
            .maxCount(1)
            .fireproof());

        Crying.register(this, "crying_grappling_hook");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> itemGroup.addAfter(Crying.EYE_CONNECTED_TO_A_STICK, this));
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        try {
            if (world.getDimensionEntry().getKey().get() != Crying.CRYING_DIMENSION_TYPE) {
                return ActionResult.FAIL;
            }   
        }
        catch (Exception e) {
            return ActionResult.FAIL;
        }
        ItemStack stack = user.getStackInHand(hand);
        HookVars ihook = Crying.getHook(user);
        GrapplingHookEntity hookEntity = ihook.getHook();
        if (hookEntity != null) {
            hookEntity.discard();
            user.swingHand(hand, user instanceof ServerPlayerEntity);
            return ActionResult.PASS;
        }

        if (!world.isClient()) {
            GrapplingHookEntity hook = GrapplingHookEntity.createWithOwner(Crying.GRAPPLING_HOOK, user, world);
            stack.set(Crying.THROWN, true);
            stack.set(Crying.HOOK_UUID, hook.getUuidAsString());
            Vec3d look = user.getRotationVec(1.0F);
            hook.setPos(user.getX() + look.x * 0.6, user.getEyeY() - 0.1 + look.y * 0.6, user.getZ() + look.z * 0.6);
            float speed = 1.8f;
            hook.setVelocity(look.x * speed, look.y * speed, look.z * speed);
            world.spawnEntity(hook);
            stack.damage(1, user);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}