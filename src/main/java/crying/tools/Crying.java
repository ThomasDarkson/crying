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
import crying.tools.blocks.OverHardenedCoreWithEye;
import crying.tools.effects.LoveOfTheFeline;
import crying.tools.enchantments.Aegis;
import crying.tools.enchantments.BaneOfCriers;
import crying.tools.enchantments.Bloodlust;
import crying.tools.enchantments.Feathered;
import crying.tools.enchantments.Smoothness;
import crying.tools.entities.CrierEntity;
import crying.tools.items.CrierSpawnEgg;
import crying.tools.items.CryingApple;
import crying.tools.items.CryingIngot;
import crying.tools.items.CryingResidue;
import crying.tools.items.CryingRod;
import crying.tools.items.Eye;
import crying.tools.other.CryingEnchantmentTags;
import crying.tools.other.CryingLoot;
import crying.tools.other.CryingTags;
import crying.tools.tools.CryingAxe;
import crying.tools.tools.CryingHoe;
import crying.tools.tools.CryingPickaxe;
import crying.tools.tools.CryingShovel;
import crying.tools.tools.CryingSword;
import crying.tools.tools.TheCryingBeing;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.minecraft.world.World;

public class Crying implements ModInitializer {
    public static final String ID = "crying";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@SuppressWarnings("rawtypes")
	static RegistryKey key(String id) {
		return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, id));
	}

	@SuppressWarnings("unchecked")
	public static final EntityType<CrierEntity> CRIER = Registry.register(
		Registries.ENTITY_TYPE,
		key("crier"),
		EntityType.Builder.create(CrierEntity::new, SpawnGroup.MONSTER).dimensions(0.58F, 1.98F).eyeHeight(1.75F).build(key("crier"))
	);

	public static Item helmet = null;
	public static Item chestplate = null;
	public static Item leggings = null;
	public static Item boots = null;

	public static Item residue = null;
	public static Item ingot = null;

	public static Item overhardenedcore = null;

	public static Item crying_apple = null;

	public static Item hoe = null;
	public static Item pickaxe = null;
	public static Item axe = null;
	public static Item sword = null;

	public static Item THE_CRYING_BEING = null;

	public static Item EYE = null;

	public static Block HARD_CRYING_OBSIDIAN = null;

	public static final Identifier CRIER_IDLE = Identifier.of(ID, "crier_idle");
    public static SoundEvent CRIER_IDLE_EVENT = SoundEvent.of(CRIER_IDLE);

	public static final Identifier CRIER_HURT = Identifier.of(ID, "crier_hurt");
    public static SoundEvent CRIER_HURT_EVENT = SoundEvent.of(CRIER_HURT);

	public static final Identifier CRIER_DIES = Identifier.of(ID, "crier_dies");
    public static SoundEvent CRIER_DIES_EVENT = SoundEvent.of(CRIER_DIES);

	public static final Identifier CRIER_SCREAM = Identifier.of(ID, "crier_scream");
    public static SoundEvent CRIER_SCREAM_EVENT = SoundEvent.of(CRIER_SCREAM);

	@Override
	public void onInitialize() {
		Feathered.initialize();
		Bloodlust.initialize();
		Aegis.initialize();

		// Block
		new CryingBlock();
		HARD_CRYING_OBSIDIAN = new HardCryingObsidian();

		ingot = new CryingIngot().item;

		// Ore
		new CryingOre();
		residue = new CryingResidue().item;

        // Tool
		pickaxe = new CryingPickaxe();
		axe = new CryingAxe();
		new CryingShovel();
		hoe = new CryingHoe();

		crying.tools.effects.BaneOfCriers.initialize();
		LoveOfTheFeline.initialize();
		sword = new CryingSword();

        boots = new CryingBoots();
        leggings = new CryingLeggings();
        chestplate = new CryingChestplate();
        helmet = new CryingHelmet();

		crying_apple = new CryingApple();

		new CryingRod();
		overhardenedcore = new OverHardenedCore().asItem();
		
		THE_CRYING_BEING = new TheCryingBeing();

		CryingEnchantmentTags.initialize();

		CryingLoot.modifyLootTables();

		CryingTags.initialize();
		
		BaneOfCriers.initialize();
		Smoothness.initialize();

		Registry.register(Registries.SOUND_EVENT, CRIER_IDLE, CRIER_IDLE_EVENT);
		Registry.register(Registries.SOUND_EVENT, CRIER_HURT, CRIER_HURT_EVENT);
		Registry.register(Registries.SOUND_EVENT, CRIER_DIES, CRIER_DIES_EVENT);

		EYE = new Eye();
		new OverHardenedCoreWithEye();

		FabricDefaultAttributeRegistry.register(CRIER, CrierEntity.createCrierAttributes());

		new CrierSpawnEgg();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
			try {
				server.getPlayerManager().getPlayerList().forEach(player -> {
					if (player instanceof ServerPlayerEntity serverPlayer) {
						CryingArmor.setCount(serverPlayer);
					}
				});
			}
			catch(Exception e) {

			}
        });

		EntityElytraEvents.CUSTOM.register((entity, tick) -> {
			try {
				World world = entity.getWorld();
				ItemStack mainStack = entity.getMainHandStack();
				int level = EnchantmentHelper.getLevel(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Feathered.FEATHERED), mainStack);
				return level > 0;
			}
			catch(Exception e) {
				return false;
			}
		});
	}

    public static Item register(Item item, String id) {
		Identifier itemID = Identifier.of(Crying.ID, id);
		Item registeredItem = Registry.register(Registries.ITEM, itemID, item);
		return registeredItem;
	}

	public static Block registerBlock(Block block, String name, boolean shouldRegisterItem) {
		Identifier id = Identifier.of(Crying.ID, name);
		Rarity rarity = Rarity.COMMON;
		Integer stack = 64;

		Item.Settings settings = new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, name)));

		if (name == "over-hardened_core" || name == "over-hardened_core_with_eye") {
			stack = 1;
			settings = settings.fireproof();
		}

		settings = settings.rarity(rarity);
		settings = settings.maxCount(stack);

		if (shouldRegisterItem) {
			BlockItem blockItem = new BlockItem(block, settings);
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