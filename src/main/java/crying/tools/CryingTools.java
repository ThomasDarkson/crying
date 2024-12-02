package crying.tools;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class CryingTools implements ModInitializer {
	public static final String MOD_ID = "crying";

	public static final Item[] itemsAllowed = {
		Items.NETHERITE_AXE, 
		Items.NETHERITE_SWORD, 
		Items.NETHERITE_PICKAXE, 
		Items.NETHERITE_HOE, 
		Items.NETHERITE_SHOVEL, 
	}; // Make it so netherite tools can be placed into smithing table slots

	public static Item ingot = null;

	public static Item upgrade = null;

	@Override
	public void onInitialize() {
		// Create every tool and block 
		ingot = new CryingIngot().item;
		new CryingBlock();
		upgrade = new CryingUpgrade();
		new CryingPickaxe();
		new CryingSword();
		new CryingAxe();
		new CryingShovel();
		new CryingHoe();

		CryingLoot.modifyLootTables();
	}

	public static Item register(Item item, String id) {
		Identifier itemID = Identifier.of(MOD_ID, id);
		Item registeredItem = Registry.register(Registries.ITEM, itemID, item);
		return registeredItem;
	}

	public static Block registerBlock(Block block, String name, boolean shouldRegisterItem) {
		Identifier id = Identifier.of(MOD_ID, name);

		if (shouldRegisterItem) {
			BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, name))));
			Registry.register(Registries.ITEM, id, blockItem);
		}

		return Registry.register(Registries.BLOCK, id, block);
	}
}