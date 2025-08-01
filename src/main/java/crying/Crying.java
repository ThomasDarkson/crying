package crying;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crying.armors.CryingArmor;
import crying.armors.CryingBootsItem;
import crying.armors.CryingChestplateItem;
import crying.armors.CryingChestplateWithElytraItem;
import crying.armors.CryingHelmetItem;
import crying.armors.CryingHorseArmor;
import crying.armors.CryingLeggingsItem;
import crying.blocks.CriersHeartBlock;
import crying.blocks.CryingBlock;
import crying.blocks.CryingOreBlock;
import crying.blocks.HardCryingObsidianBlock;
import crying.blocks.OverHardenedCoreBlock;
import crying.blocks.OverHardenedCoreWithEyeBlock;
import crying.effects.BaneOfCriers;
import crying.entities.*;
import crying.interfaces.CryingTool;
import crying.items.CrierSpawnEggItem;
import crying.items.CryingAppleItem;
import crying.items.CryingIngotItem;
import crying.items.CryingResidueItem;
import crying.items.EyeItem;
import crying.items.GranterItem;
import crying.other.CryingLoot;
import crying.other.CryingTags;
import crying.tools.axe.*;
import crying.tools.hoe.*;
import crying.tools.pickaxe.*;
import crying.tools.shovel.*;
import crying.tools.sword.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class Crying implements ModInitializer {
    public static final String ID = "crying";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static final EntityType<CrierEntity> CRIER = Registry.register(
		Registries.ENTITY_TYPE,
		RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, "crier")),
		EntityType.Builder.create(CrierEntity::new, SpawnGroup.MONSTER).dimensions(0.58F, 1.98F).eyeHeight(1.75F).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, "crier")))
	);

	public static final EntityType<GranterEntity> GRANTER_ENTITY = Registry.register(
		Registries.ENTITY_TYPE,
		RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, "granter")),
		EntityType.Builder.create(GranterEntity::new, SpawnGroup.MISC).dimensions(0.2F, 0.2F).trackingTickInterval(1).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, "granter")))
	);

	public static final Block HEART = new CriersHeartBlock(Settings.create().
            registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Crying.ID, "criers_heart"))).
            strength(0F, 36000000.0F).
            luminance((state) -> {
                return 2;
            }));

	public static final BlockEntityType<CriersHeartBlockEntity> CRIERS_HEART = registerBlockEntityType(
		"criers_heart", 
		FabricBlockEntityTypeBuilder.<CriersHeartBlockEntity>create(CriersHeartBlockEntity::new, HEART).build()
	);

	public static final Item CRYING_HELMET;
	public static final Item CRYING_CHESTPLATE;
	public static final Item CRYING_CHESTPLATE_WITH_ELYTRA;
	public static final Item CRYING_LEGGINGS;
	public static final Item CRYING_BOOTS;
	public static final Item CRYING_HORSE_ARMOR;

	public static final Item GRANTER;

	public static final Item CRYING_RESIDUE;
	public static final Item CRYING_INGOT;

	public static final Item CRYING_APPLE;

	public static final Item CRYING_PICKAXE_OVER_HARDENED_CORE_WITH_EYE;
	public static final Item CRYING_PICKAXE;
	public static final Item CRYING_PICKAXE_NETHERITE;
	public static final Item CRYING_PICKAXE_DIAMOND;
	public static final Item CRYING_PICKAXE_IRON;
	public static final Item CRYING_PICKAXE_GOLD;

	public static final Item CRIERS_SWORD;
	public static final Item CRYING_SWORD;
	public static final Item CRYING_SWORD_NETHERITE;
	public static final Item CRYING_SWORD_DIAMOND;
	public static final Item CRYING_SWORD_IRON;
	public static final Item CRYING_SWORD_GOLD;

	public static final Item CRYING_AXE;
	public static final Item CRYING_AXE_NETHERITE;
	public static final Item CRYING_AXE_DIAMOND;
	public static final Item CRYING_AXE_IRON;
	public static final Item CRYING_AXE_GOLD;

	public static final Item CRYING_SHOVEL;
	public static final Item CRYING_SHOVEL_NETHERITE;
	public static final Item CRYING_SHOVEL_DIAMOND;
	public static final Item CRYING_SHOVEL_IRON;
	public static final Item CRYING_SHOVEL_GOLD;

	public static final Item CRYING_HOE;
	public static final Item CRYING_HOE_NETHERITE;
	public static final Item CRYING_HOE_DIAMOND;
	public static final Item CRYING_HOE_IRON;
	public static final Item CRYING_HOE_GOLD;

	public static final Item EYE;

	public static final Block OVER_HARDENED_CORE;
	public static final Block OVER_HARDENED_CORE_WITH_EYE;
	public static final Block HARD_CRYING_OBSIDIAN;
	public static final Block CRYING_BLOCK;
	public static final Block CRYING_ORE;

	public static final Item CRIER_SPAWN_EGG;

	public static final Identifier CRIER_IDLE = Identifier.of(ID, "crier_idle");
    public static final SoundEvent CRIER_IDLE_EVENT = SoundEvent.of(CRIER_IDLE);

	public static final Identifier CRIER_HURT = Identifier.of(ID, "crier_hurt");
    public static final SoundEvent CRIER_HURT_EVENT = SoundEvent.of(CRIER_HURT);

	public static final Identifier CRIER_DIES = Identifier.of(ID, "crier_dies");
    public static final SoundEvent CRIER_DIES_EVENT = SoundEvent.of(CRIER_DIES);

	public static final Identifier CRIER_SCREAM = Identifier.of(ID, "crier_scream");
    public static final SoundEvent CRIER_SCREAM_EVENT = SoundEvent.of(CRIER_SCREAM);

	public static final Identifier GRANTER_HEAL = Identifier.of(ID, "granter_heal");
	public static final SoundEvent GRANTER_HEAL_EVENT = SoundEvent.of(GRANTER_HEAL);

	static {
		//Blocks
		CRYING_BLOCK = new CryingBlock();
		HARD_CRYING_OBSIDIAN = new HardCryingObsidianBlock();
		CRYING_ORE = new CryingOreBlock();
		OVER_HARDENED_CORE_WITH_EYE = new OverHardenedCoreWithEyeBlock();
		OVER_HARDENED_CORE = new OverHardenedCoreBlock();

		// Items
		CRYING_INGOT = new CryingIngotItem();
		CRYING_RESIDUE = new CryingResidueItem();
		CRYING_APPLE = new CryingAppleItem();
		EYE = new EyeItem();
		CRIER_SPAWN_EGG = new CrierSpawnEggItem();
		GRANTER = new GranterItem();

        // Tools
		CRYING_PICKAXE_OVER_HARDENED_CORE_WITH_EYE = new CryingPickaxeOverHardenedCoreWithEyeItem();
		CRYING_PICKAXE = new CryingPickaxeItem();
		CRYING_PICKAXE_NETHERITE = new CryingPickaxeNetheriteItem();
		CRYING_PICKAXE_DIAMOND = new CryingPickaxeDiamondItem();
		CRYING_PICKAXE_IRON = new CryingPickaxeIronItem();
		CRYING_PICKAXE_GOLD = new CryingPickaxeGoldItem();

		CRIERS_SWORD = new CriersSwordItem();
		CRYING_SWORD = new CryingSwordItem();
		CRYING_SWORD_NETHERITE = new CryingSwordNetheriteItem();
		CRYING_SWORD_DIAMOND = new CryingSwordDiamondItem();
		CRYING_SWORD_IRON = new CryingSwordIronItem();
		CRYING_SWORD_GOLD = new CryingSwordGoldItem();

		CRYING_AXE = new CryingAxeItem();
		CRYING_AXE_NETHERITE = new CryingAxeNetheriteItem();
		CRYING_AXE_DIAMOND = new CryingAxeDiamondItem();
		CRYING_AXE_IRON = new CryingAxeIronItem();
		CRYING_AXE_GOLD = new CryingAxeGoldItem();

		CRYING_SHOVEL = new CryingShovelItem();
		CRYING_SHOVEL_NETHERITE = new CryingShovelNetheriteItem();
		CRYING_SHOVEL_DIAMOND = new CryingShovelDiamondItem();
		CRYING_SHOVEL_IRON = new CryingShovelIronItem();
		CRYING_SHOVEL_GOLD = new CryingShovelGoldItem();

		CRYING_HOE = new CryingHoeItem();
		CRYING_HOE_NETHERITE = new CryingHoeNetheriteItem();
		CRYING_HOE_DIAMOND = new CryingHoeDiamondItem();
		CRYING_HOE_IRON = new CryingHoeIronItem();
		CRYING_HOE_GOLD = new CryingHoeGoldItem();

		// Armors
		CRYING_BOOTS = new CryingBootsItem();
        CRYING_LEGGINGS = new CryingLeggingsItem();
        CRYING_CHESTPLATE = new CryingChestplateItem();
		CRYING_CHESTPLATE_WITH_ELYTRA = new CryingChestplateWithElytraItem();
        CRYING_HELMET = new CryingHelmetItem();
		CRYING_HORSE_ARMOR = new CryingHorseArmor();
	}

	@Override
	public void onInitialize() {
		BaneOfCriers.initialize();
		crying.enchantments.BaneOfCriers.initialize();

		CryingLoot.modifyLootTables();

		CryingTags.initialize();

		Registry.register(Registries.SOUND_EVENT, CRIER_IDLE, CRIER_IDLE_EVENT);
		Registry.register(Registries.SOUND_EVENT, CRIER_HURT, CRIER_HURT_EVENT);
		Registry.register(Registries.SOUND_EVENT, CRIER_DIES, CRIER_DIES_EVENT);
		Registry.register(Registries.SOUND_EVENT, GRANTER_HEAL, GRANTER_HEAL_EVENT);

		FabricDefaultAttributeRegistry.register(CRIER, CrierEntity.createCrierAttributes());

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
				ItemStack mainStack = entity.getEquippedStack(EquipmentSlot.CHEST);
				return mainStack.getItem() instanceof CryingChestplateWithElytraItem;
			}
			catch(Exception e) {
				return false;
			}
		});
	}

    public static final Item register(Item item, String id) {
		Identifier itemID = Identifier.of(Crying.ID, id);
		Item registeredItem = Registry.register(Registries.ITEM, itemID, item);
		return registeredItem;
	}

	public static Block registerBlock(Block block, String name) {
		Identifier id = Identifier.of(Crying.ID, name);
		Rarity rarity = Rarity.COMMON;
		Integer stack = 64;

		Item.Settings settings = new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, name)));

		if (name == "over-hardened_core" || name == "over-hardened_core_with_eye" || name == "criers_heart") {
			stack = 1;
			rarity = Rarity.EPIC;
			settings = settings.fireproof();
		}

		settings = settings.rarity(rarity);
		settings = settings.maxCount(stack);
		
		BlockItem blockItem = new BlockItem(block, settings);
		Registry.register(Registries.ITEM, id, blockItem);

		return Registry.register(Registries.BLOCK, id, block);
	}

	public static <T extends BlockEntityType<?>> T registerBlockEntityType(String path, T blockEntityType) {
    	return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("tutorial", path), blockEntityType);
	}
 
    public static ArmorMaterial registerMaterial(int durability, Map<EquipmentType, Integer> defensePoints, int enchantability, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> tagKey, RegistryKey<EquipmentAsset> registr) {
        ArmorMaterial material = new ArmorMaterial(durability, defensePoints, enchantability, equipSound, toughness, knockbackResistance, tagKey, registr);
		return material;
    }

	public static Item getCoreIngredient(ItemStack stack) {
		if (stack.isEmpty())
			LOGGER.info("crying: EMPTY STACK ERROR");

		if (stack.getItem() instanceof CryingTool tool)
			return tool.getCoreIngredient();
		return CRYING_INGOT;
	}

	public static boolean isTheCriersSword(ItemStack stack) {
		if (stack  == null)
			return false;
		return stack.getItem() instanceof AbstractCryingSwordItem && getCoreIngredient(stack) == OVER_HARDENED_CORE_WITH_EYE.asItem();
	}

	public static boolean isTheCryingBeing(ItemStack stack) {
		if (stack  == null)
			return false;
		return stack.getItem() instanceof AbstractCryingPickaxeItem && getCoreIngredient(stack) == OVER_HARDENED_CORE_WITH_EYE.asItem();
	}

	public static MutableText getCoreIngredientText(ItemStack stack) {
		Item item = getCoreIngredient(stack);
		if (item == Items.IRON_INGOT)
			return Text.translatable("core.ingredient.iron").setStyle(Style.EMPTY.withColor(Formatting.GRAY));
		else if (item == Items.DIAMOND)
			return Text.translatable("core.ingredient.diamond").setStyle(Style.EMPTY.withColor(Formatting.AQUA));
		else if (item == Items.NETHERITE_INGOT)
			return Text.translatable("core.ingredient.netherite").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
		else if (item == Items.GOLD_INGOT)
			return Text.translatable("core.ingredient.gold").setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
		else if (item == OVER_HARDENED_CORE_WITH_EYE.asItem())
			return Text.translatable("item.crying.over-hardened_core_with_eye").setStyle(Style.EMPTY.withColor(1966200));

		return Text.translatable("core.ingredient.crying").setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE));
	}

	public static float nextBetween(float min, float max) {
		Random random = new Random();
		float f = random.nextFloat(max - min + 1) + min;
		return f;
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

	public static int tickSecond(int second) {
		return Math.abs(second * 20);
	}

	public static String tickToString(int ticks) {
		int totalSeconds = ticks / 20;
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}
}