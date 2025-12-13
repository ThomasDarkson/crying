package crying.armors;

import crying.Crying;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public class CryingAssetKeys implements EquipmentAssets {
    static ResourceKey<? extends Registry<EquipmentAsset>> REGISTRY_KEY = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));
    public static ResourceKey<EquipmentAsset> CRYING = createId("crying");
    public static ResourceKey<EquipmentAsset> CRYING_WITH_ELYTRA = createId("crying_with_elytra");

    static ResourceKey<EquipmentAsset> createId(String name) {
        return ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath(Crying.ID, name));
    }
}