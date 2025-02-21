package crying.tools;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crying.tools.armors.CryingArmor;
import crying.tools.armors.CryingBoots;
import crying.tools.armors.CryingChestplate;
import crying.tools.armors.CryingHelmet;
import crying.tools.armors.CryingLeggings;
import crying.tools.blocks.CryingBlock;
import crying.tools.blocks.CryingOre;
import crying.tools.blocks.HardCryingObsidian;
import crying.tools.blocks.OverHardenedCore;
import crying.tools.enchantments.BaneOfCriers;
import crying.tools.enchantments.Smoothness;
import crying.tools.items.CryingApple;
import crying.tools.items.CryingIngot;
import crying.tools.items.CryingResidue;
import crying.tools.items.CryingRod;
import crying.tools.items.CryingUpgrade;
import crying.tools.items.Handle;
import crying.tools.effects.Crier;
import crying.tools.other.CryingLoot;
import crying.tools.other.CryingTags;
import crying.tools.tools.CryingAxe;
import crying.tools.tools.CryingHoe;
import crying.tools.tools.CryingKnife;
import crying.tools.tools.CryingPickaxe;
import crying.tools.tools.CryingShovel;
import crying.tools.tools.CryingSword;
import crying.tools.tools.Knife;
import crying.tools.tools.TheCryingBeing;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class Crying implements ModInitializer {
    public static final String MOD_ID = "crying";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Item[] itemsAllowed = {
		Items.NETHERITE_AXE, 
		Items.NETHERITE_SWORD, 
		Items.NETHERITE_PICKAXE, 
		Items.NETHERITE_HOE, 
		Items.NETHERITE_SHOVEL, 
	}; // Make it so netherite tools can be placed into smithing table slots

	public static Item residue = null;
	public static Item ingot = null;

	public static Item upgrade = null;

	public static Item crying_apple = null;

	public static Item hoe = null;
	public static Item pickaxe = null;
	public static Item axe = null;
	public static SwordItem sword = null;

	public static SwordItem knife = null;
	public static SwordItem crying_knife = null;

	public static Item THE_CRYING_BEING = null;

	@Override
	public void onInitialize() {
		// Block
		new CryingBlock();
		new HardCryingObsidian();

		// Upgrade
		ingot = new CryingIngot().item;
		upgrade = new CryingUpgrade();

		// Ore
		new CryingOre();
		residue = new CryingResidue().item;

        // Tool
		pickaxe = new CryingPickaxe();
		axe = new CryingAxe();
		new CryingShovel();
		hoe = new CryingHoe();

		crying.tools.effects.BaneOfCriers.initialize();
		sword = new CryingSword();

		// Knives
		new Handle();
		knife = new Knife();
		crying_knife = new CryingKnife();

        // Armor
		Crier.setupCrier();
        new CryingBoots();
        new CryingLeggings();
        new CryingChestplate();
        new CryingHelmet();

		crying_apple = new CryingApple();

		new CryingRod();
		new OverHardenedCore();
		
		THE_CRYING_BEING = new TheCryingBeing();

		CryingLoot.modifyLootTables();

		CryingTags.initialize();
		
		BaneOfCriers.initialize();
		Smoothness.initialize();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    CryingArmor.checkAndApplyArmorEffect(serverPlayer);
                }
            });
        });
	}

    public static Item register(Item item, String id) {
		Identifier itemID = Identifier.of(Crying.MOD_ID, id);
		Item registeredItem = Registry.register(Registries.ITEM, itemID, item);
		return registeredItem;
	}

	public static Block registerBlock(Block block, String name, boolean shouldRegisterItem) {
		Identifier id = Identifier.of(Crying.MOD_ID, name);
		Rarity rarity = Rarity.COMMON;
		Integer stack = 64;

		if (name == "over-hardened_core") {
			rarity = Rarity.EPIC;
			stack = 1;
		}

		if (shouldRegisterItem) {
			BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.MOD_ID, name))).rarity(rarity).maxCount(stack));
			Registry.register(Registries.ITEM, id, blockItem);
		}

		return Registry.register(Registries.BLOCK, id, block);
	}

    public static ArmorMaterial registerMaterial(int durability, Map<EquipmentType, Integer> defensePoints, int enchantability, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> tagKey, RegistryKey<EquipmentAsset> registr) {
        ArmorMaterial material = new ArmorMaterial(durability, defensePoints, enchantability, equipSound, toughness, knockbackResistance, tagKey, registr);
		return material;
    }

	public static float floorDecimal(float value, int decimals) {
		if (decimals < 1)
			return (float) Math.floor(value);

		float mult = 0F;
		for (int m = 0; m < decimals; m++) {
			mult += 10F;
		}

		float newValue = (float) Math.floor(value * mult);
		return newValue / mult;
	}
}