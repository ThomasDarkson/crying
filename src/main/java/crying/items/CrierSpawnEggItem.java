package crying.items;

import java.util.Optional;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.phys.Vec3;

public class CrierSpawnEggItem extends SpawnEggItem {
    public CrierSpawnEggItem() {
        super(new Item.Properties()
            .spawnEgg(Crying.CRIER)
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Crying.ID, "crier_spawn_egg"))));

        Crying.register(this, "crier_spawn_egg");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register((itemGroup) -> itemGroup.accept(this));
    }

    @Override
    public Optional<Mob> spawnOffspringFromSpawnEgg(Player user, Mob entity, EntityType<? extends Mob> entityType, ServerLevel world, Vec3 pos, ItemStack stack) {
        return Optional.empty();
    }
}