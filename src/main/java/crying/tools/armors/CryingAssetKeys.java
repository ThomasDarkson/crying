package crying.tools.armors;

import crying.tools.Crying;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CryingAssetKeys implements EquipmentAssetKeys {
   static RegistryKey<? extends Registry<EquipmentAsset>> REGISTRY_KEY = RegistryKey.ofRegistry(Identifier.ofVanilla("equipment_asset"));
   public static RegistryKey<EquipmentAsset> CRYING = register("crying");

   static RegistryKey<EquipmentAsset> register(String name) {
      return RegistryKey.of(REGISTRY_KEY, Identifier.of(Crying.MOD_ID, name));
   }
}