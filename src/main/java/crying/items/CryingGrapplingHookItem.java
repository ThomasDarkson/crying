package crying.items;

import crying.Crying;
import crying.entities.GrapplingHookEntity;
import crying.interfaces.HookVars;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CryingGrapplingHookItem extends Item {
    public CryingGrapplingHookItem() {
        super(new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_grappling_hook")))
            .component(Crying.THROWN, false)
            .component(Crying.HOOK_UUID, "")
            .rarity(Rarity.EPIC)
            .stacksTo(1)
            .fireResistant());

        Crying.register(this, "crying_grappling_hook");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((itemGroup) -> itemGroup.addAfter(Crying.EYE_CONNECTED_TO_A_STICK, this));
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        try {
            if (world.dimensionTypeRegistration().unwrapKey().get() != Crying.CRYING_DIMENSION_TYPE) {
                return InteractionResult.FAIL;
            }   
        }
        catch (Exception e) {
            return InteractionResult.FAIL;
        }
        ItemStack stack = user.getItemInHand(hand);
        HookVars ihook = Crying.getHook(user);
        GrapplingHookEntity hookEntity = ihook.getHook();
        if (hookEntity != null) {
            hookEntity.discard();
            user.swing(hand, user instanceof ServerPlayer);
            return InteractionResult.PASS;
        }

        if (!world.isClientSide()) {
            GrapplingHookEntity hook = GrapplingHookEntity.createWithOwner(Crying.GRAPPLING_HOOK, user, world);
            stack.set(Crying.THROWN, true);
            stack.set(Crying.HOOK_UUID, hook.getStringUUID());
            Vec3 look = user.getViewVector(1.0F);
            hook.setPosRaw(user.getX() + look.x * 0.6, user.getEyeY() - 0.1 + look.y * 0.6, user.getZ() + look.z * 0.6);
            float speed = 1.8f;
            hook.setDeltaMovement(look.x * speed, look.y * speed, look.z * speed);
            world.addFreshEntity(hook);
            stack.hurtWithoutBreaking(1, user);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}