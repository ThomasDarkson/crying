package crying;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;

import crying.armors.CryingArmor;
import crying.armors.CryingBootsItem;
import crying.armors.CryingChestplateItem;
import crying.armors.CryingChestplateWithElytraItem;
import crying.armors.CryingHelmetItem;
import crying.armors.CryingHorseArmor;
import crying.armors.CryingLeggingsItem;
import crying.backend.CollapsingReasonArgumentType;
import crying.blocks.CriersHeartBlock;
import crying.blocks.CryingBlock;
import crying.blocks.CryingOreBlock;
import crying.blocks.HardCryingObsidianBlock;
import crying.blocks.LostCryingBlock;
import crying.blocks.VoidStainedCryingBlock;
import crying.blocks.core.HardenedCoreBlock;
import crying.blocks.core.OverHardenedCoreBlock;
import crying.blocks.core.OverHardenedCoreWithEyeBlock;
import crying.blocks.food.CryingFoodAppleBlock;
import crying.blocks.food.CryingFoodCarrotBlock;
import crying.blocks.food.CryingFoodEnchantedAppleBlock;
import crying.effects.BaneOfCriers;
import crying.entities.*;
import crying.enums.ToolType;
import crying.interfaces.CryingTool;
import crying.interfaces.HookVars;
import crying.interfaces.SanityVars;
import crying.interfaces.SanityManager;
import crying.items.CrierSpawnEggItem;
import crying.items.CryingAppleItem;
import crying.items.CryingCarrotItem;
import crying.items.CryingResidueItem;
import crying.items.EnchantedCryingAppleItem;
import crying.items.EyeConnectedToAStickItem;
import crying.items.EyeItem;
import crying.items.GranterItem;
import crying.items.CryingGrapplingHookItem;
import crying.items.CryingGrapplingHookTipItem;
import crying.items.HardenedCorePieceItem;
import crying.items.LostCrierSpawnEggItem;
import crying.items.ingot.AbstractCryingIngotItem;
import crying.items.ingot.CryingIngotCopperItem;
import crying.items.ingot.CryingIngotDiamondItem;
import crying.items.ingot.CryingIngotGoldItem;
import crying.items.ingot.CryingIngotIronItem;
import crying.items.ingot.CryingIngotItem;
import crying.items.ingot.CryingIngotNetheriteItem;
import crying.other.CryingLoot;
import crying.other.CryingTags;
import crying.tools.CryingShieldItem;
import crying.tools.axe.*;
import crying.tools.hoe.*;
import crying.tools.pickaxe.*;
import crying.tools.shovel.*;
import crying.tools.sword.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import semantic.ver.lib.SemanticVerLib;
import semantic.ver.lib.SemanticVersion;
import static net.minecraft.server.command.CommandManager.*;

public class Crying implements ModInitializer {
	private static final String VERSION_URL = "https://raw.githubusercontent.com/ThomasDarkson/crying/refs/heads/version/version.txt";
	public static final SemanticVersion VERSION = SemanticVersion.stable(6, 1, 1, VERSION_URL);
    public static final String ID = "crying";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final int MAX_CRYING_FOOD_COUNT = 9888;

	public static final TrackedData<Integer> FOOD_COUNT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public static final EntityType<CrierEntity> CRIER = Registry.register(
		Registries.ENTITY_TYPE,
		RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, "crier")),
		EntityType.Builder.create(CrierEntity::new, SpawnGroup.MONSTER).dimensions(0.58F, 1.98F).eyeHeight(1.75F).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, "crier")))
	);

	public static final EntityType<LostCrierEntity> LOST_CRIER = Registry.register(
		Registries.ENTITY_TYPE,
		RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, "lost_crier")),
		EntityType.Builder.create(LostCrierEntity::new, SpawnGroup.CREATURE).dimensions(0.58F, 1.98F).eyeHeight(1.75F).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, "lost_crier")))
	);

	public static final EntityType<GrapplingHookEntity> GRAPPLING_HOOK = Registry.register(
		Registries.ENTITY_TYPE,
		RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, "grappling_hook_entity")),
        EntityType.Builder.create(GrapplingHookEntity::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).maxTrackingRange(8).trackingTickInterval(1).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, "grappling_hook_entity")))
    );

	public static final EntityType<GranterEntity> GRANTER_ENTITY = Registry.register(
		Registries.ENTITY_TYPE,
		RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, "granter")),
		EntityType.Builder.create(GranterEntity::new, SpawnGroup.MISC).dimensions(0.2F, 0.2F).trackingTickInterval(1).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ID, "granter")))
	);

	public static final Block CRIERS_HEART = new CriersHeartBlock(Settings.create().
            registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Crying.ID, "criers_heart"))).
            strength(0F, 36000000.0F).
            luminance((state) -> {
                return 2;
            }));

	public static final BlockEntityType<CriersHeartBlockEntity> CRIERS_HEART_BLOCK_ENTITY = registerBlockEntityType(
		"criers_heart", 
		FabricBlockEntityTypeBuilder.<CriersHeartBlockEntity>create(CriersHeartBlockEntity::new, CRIERS_HEART).build()
	);

	public static final Block CRYING_FOOD_CARROT = new CryingFoodCarrotBlock();
	public static final Block CRYING_FOOD_APPLE = new CryingFoodAppleBlock();
	public static final Block CRYING_FOOD_ENCHANTED_APPLE = new CryingFoodEnchantedAppleBlock();

	public static final BlockEntityType<CryingFoodEntity> CRYING_FOOD_BLOCK_ENTITY = registerBlockEntityType(
		"crying_food_block_entity", 
		FabricBlockEntityTypeBuilder.<CryingFoodEntity>create(CryingFoodEntity::new, CRYING_FOOD_CARROT, CRYING_FOOD_APPLE, CRYING_FOOD_ENCHANTED_APPLE).build()
	);

	public static final RegistryKey<Biome> SOMEWHAT_WEIRD_ISLAND = RegistryKey.of(RegistryKeys.BIOME, Identifier.of(ID, "somewhat_weird_island"));
	public static final RegistryKey<Biome> SOMEWHAT_NORMAL_ISLAND = RegistryKey.of(RegistryKeys.BIOME, Identifier.of(ID, "somewhat_normal_island"));
	public static final RegistryKey<World> CRYING_WORLD = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(ID, "crying"));
	public static final RegistryKey<DimensionOptions> CRYING_DIMENSION = RegistryKey.of(RegistryKeys.DIMENSION, Identifier.of(ID, "crying"));
	public static final RegistryKey<DimensionType> CRYING_DIMENSION_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, Identifier.of(ID, "crying"));

	public static final ComponentType<Boolean> WAS_WAXED;
	public static final ComponentType<Integer> OXIDATION_SECONDS;
	public static final ComponentType<String> OXIDATION_LEVEL;
	public static final ComponentType<Boolean> THROWN;
	public static final ComponentType<String> HOOK_UUID;
	
	public static final Item CRYING_SHIELD;

	public static final Item CRYING_HELMET;
	public static final Item CRYING_CHESTPLATE;
	public static final Item CRYING_CHESTPLATE_WITH_ELYTRA;
	public static final Item CRYING_LEGGINGS;
	public static final Item CRYING_BOOTS;
	public static final Item CRYING_HORSE_ARMOR;

	public static final Item GRANTER;

	public static final Item CRYING_GRAPPLING_HOOK;
	public static final Item CRYING_GRAPPLING_HOOK_TIP;

	public static final Item CRYING_RESIDUE;
	public static final Item CRYING_INGOT;
	public static final Item CRYING_INGOT_NETHERITE;
	public static final Item CRYING_INGOT_DIAMOND;
	public static final Item CRYING_INGOT_IRON;
	public static final Item CRYING_INGOT_COPPER;
	public static final Item CRYING_INGOT_GOLD;

	public static final Item CRYING_APPLE;
	public static final Item CRYING_CARROT;
	public static final Item ENCHANTED_CRYING_APPLE;

	public static final Item CRYING_PICKAXE_OVER_HARDENED_CORE_WITH_EYE;
	public static final Item CRYING_PICKAXE;
	public static final Item CRYING_PICKAXE_NETHERITE;
	public static final Item CRYING_PICKAXE_DIAMOND;
	public static final Item CRYING_PICKAXE_IRON;
	public static final Item CRYING_PICKAXE_COPPER;
	public static final Item CRYING_PICKAXE_GOLD;

	public static final Item CRIERS_SWORD;
	public static final Item CRYING_SWORD;
	public static final Item CRYING_SWORD_NETHERITE;
	public static final Item CRYING_SWORD_DIAMOND;
	public static final Item CRYING_SWORD_IRON;
	public static final Item CRYING_SWORD_COPPER;
	public static final Item CRYING_SWORD_GOLD;

	public static final Item CRYING_AXE;
	public static final Item CRYING_AXE_NETHERITE;
	public static final Item CRYING_AXE_DIAMOND;
	public static final Item CRYING_AXE_IRON;
	public static final Item CRYING_AXE_COPPER;
	public static final Item CRYING_AXE_GOLD;

	public static final Item CRYING_SHOVEL;
	public static final Item CRYING_SHOVEL_NETHERITE;
	public static final Item CRYING_SHOVEL_DIAMOND;
	public static final Item CRYING_SHOVEL_IRON;
	public static final Item CRYING_SHOVEL_COPPER;
	public static final Item CRYING_SHOVEL_GOLD;

	public static final Item CRYING_HOE;
	public static final Item CRYING_HOE_NETHERITE;
	public static final Item CRYING_HOE_DIAMOND;
	public static final Item CRYING_HOE_IRON;
	public static final Item CRYING_HOE_COPPER;
	public static final Item CRYING_HOE_GOLD;

	public static final Item EYE;
	public static final Item EYE_CONNECTED_TO_A_STICK;

	public static final Item HARDENED_CORE_PIECE;

	public static final Block HARDENED_CORE;
	public static final Block OVER_HARDENED_CORE;
	public static final Block OVER_HARDENED_CORE_WITH_EYE;
	public static final Block HARD_CRYING_OBSIDIAN;
	public static final Block CRYING_BLOCK;
	public static final Block CRYING_ORE;
	public static final Block VOID_STAINED_CRYING_BLOCK;
	public static final Block LOST_CRYING_BLOCK;

	public static final Item CRIER_SPAWN_EGG;
	public static final Item LOST_CRIER_SPAWN_EGG;

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

	public static final Reference<SoundEvent> LIVING_MICE = Registry.registerReference(Registries.SOUND_EVENT, Identifier.of(ID, "living_mice_crying_biome"), SoundEvent.of(Identifier.of(ID, "living_mice_crying_biome")));
	public static final Reference<SoundEvent> CRYING_MICE = Registry.registerReference(Registries.SOUND_EVENT, Identifier.of(ID, "crying_mice"), SoundEvent.of(Identifier.of(ID, "crying_mice")));

	static {
		WAS_WAXED = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(ID, "was_waxed"), ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN).build());
		OXIDATION_SECONDS = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(ID, "oxidation_seconds"), ComponentType.<Integer>builder().codec(Codec.intRange(0, Integer.MAX_VALUE)).build());
		OXIDATION_LEVEL = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(ID, "oxidation_level"), ComponentType.<String>builder().codec(Codec.string(0, Integer.MAX_VALUE)).build());
		THROWN = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(ID, "hook_thrown"), ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN).build());
		HOOK_UUID = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(ID, "hook_uuid"), ComponentType.<String>builder().codec(Codec.string(0, Integer.MAX_VALUE)).build());
		
		// Blocks
		CRYING_BLOCK = new CryingBlock();
		HARD_CRYING_OBSIDIAN = new HardCryingObsidianBlock();
		CRYING_ORE = new CryingOreBlock();
		OVER_HARDENED_CORE = new OverHardenedCoreBlock();
		OVER_HARDENED_CORE_WITH_EYE = new OverHardenedCoreWithEyeBlock();
		VOID_STAINED_CRYING_BLOCK = new VoidStainedCryingBlock();
		LOST_CRYING_BLOCK = new LostCryingBlock();
		HARDENED_CORE_PIECE = new HardenedCorePieceItem();
		HARDENED_CORE = new HardenedCoreBlock();

		// Items
		CRYING_RESIDUE = new CryingResidueItem();
		CRYING_INGOT = new CryingIngotItem();
		CRYING_INGOT_NETHERITE = new CryingIngotNetheriteItem();
		CRYING_INGOT_DIAMOND = new CryingIngotDiamondItem();
		CRYING_INGOT_IRON = new CryingIngotIronItem();
		CRYING_INGOT_COPPER = new CryingIngotCopperItem();
		CRYING_INGOT_GOLD = new CryingIngotGoldItem();
		CRYING_APPLE = new CryingAppleItem();
		CRYING_CARROT = new CryingCarrotItem();
		ENCHANTED_CRYING_APPLE = new EnchantedCryingAppleItem();
		EYE = new EyeItem();
		EYE_CONNECTED_TO_A_STICK = new EyeConnectedToAStickItem();
		CRIER_SPAWN_EGG = new CrierSpawnEggItem();
		LOST_CRIER_SPAWN_EGG = new LostCrierSpawnEggItem();
		GRANTER = new GranterItem();
		CRYING_GRAPPLING_HOOK = new CryingGrapplingHookItem();
		CRYING_GRAPPLING_HOOK_TIP = new CryingGrapplingHookTipItem();

        // Tools
		CRYING_PICKAXE_OVER_HARDENED_CORE_WITH_EYE = new CryingPickaxeOverHardenedCoreWithEyeItem();
		CRYING_PICKAXE = new CryingPickaxeItem();
		CRYING_PICKAXE_NETHERITE = new CryingPickaxeNetheriteItem();
		CRYING_PICKAXE_DIAMOND = new CryingPickaxeDiamondItem();
		CRYING_PICKAXE_IRON = new CryingPickaxeIronItem();
		CRYING_PICKAXE_COPPER = new CryingPickaxeCopperItem();
		CRYING_PICKAXE_GOLD = new CryingPickaxeGoldItem();

		CRIERS_SWORD = new CriersSwordItem();
		CRYING_SWORD = new CryingSwordItem();
		CRYING_SWORD_NETHERITE = new CryingSwordNetheriteItem();
		CRYING_SWORD_DIAMOND = new CryingSwordDiamondItem();
		CRYING_SWORD_IRON = new CryingSwordIronItem();
		CRYING_SWORD_COPPER = new CryingSwordCopperItem();
		CRYING_SWORD_GOLD = new CryingSwordGoldItem();

		CRYING_AXE = new CryingAxeItem();
		CRYING_AXE_NETHERITE = new CryingAxeNetheriteItem();
		CRYING_AXE_DIAMOND = new CryingAxeDiamondItem();
		CRYING_AXE_IRON = new CryingAxeIronItem();
		CRYING_AXE_COPPER = new CryingAxeCopperItem();
		CRYING_AXE_GOLD = new CryingAxeGoldItem();

		CRYING_SHOVEL = new CryingShovelItem();
		CRYING_SHOVEL_NETHERITE = new CryingShovelNetheriteItem();
		CRYING_SHOVEL_DIAMOND = new CryingShovelDiamondItem();
		CRYING_SHOVEL_IRON = new CryingShovelIronItem();
		CRYING_SHOVEL_COPPER = new CryingShovelCopperItem();
		CRYING_SHOVEL_GOLD = new CryingShovelGoldItem();

		CRYING_HOE = new CryingHoeItem();
		CRYING_HOE_NETHERITE = new CryingHoeNetheriteItem();
		CRYING_HOE_DIAMOND = new CryingHoeDiamondItem();
		CRYING_HOE_IRON = new CryingHoeIronItem();
		CRYING_HOE_COPPER = new CryingHoeCopperItem();
		CRYING_HOE_GOLD = new CryingHoeGoldItem();

		CRYING_SHIELD = new CryingShieldItem();

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
		SemanticVerLib.initialize();
		VERSION.checkForUpdates(null);
		
		BaneOfCriers.initialize();
		crying.enchantments.BaneOfCriers.initialize();

		CryingLoot.modifyLootTables();

		CryingTags.initialize();

		Registry.register(Registries.SOUND_EVENT, CRIER_IDLE, CRIER_IDLE_EVENT);
		Registry.register(Registries.SOUND_EVENT, CRIER_HURT, CRIER_HURT_EVENT);
		Registry.register(Registries.SOUND_EVENT, CRIER_DIES, CRIER_DIES_EVENT);
		Registry.register(Registries.SOUND_EVENT, GRANTER_HEAL, GRANTER_HEAL_EVENT);

		FabricDefaultAttributeRegistry.register(CRIER, CrierEntity.createCrierAttributes());
		FabricDefaultAttributeRegistry.register(LOST_CRIER, LostCrierEntity.createLostCrierAttributes());
		
		ArgumentTypeRegistry.registerArgumentType(Identifier.of(ID, "collapsing_reason"), CollapsingReasonArgumentType.class, ConstantArgumentSerializer.of(CollapsingReasonArgumentType::collapsingReason));
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("crying")
			.executes(context -> {
				context.getSource().sendFeedback(() -> Text.literal("Crying Tools ").append(VERSION.toString()), false);
				context.getSource().sendFeedback(() -> Text.translatable("crying.thank.you"), false);
				return 0;
			})
			.then(literal("sanityManager").requires(source -> source.hasPermissionLevel(2))
				.then(literal("deactivate")
					.executes(context -> {
						SanityManager manager = getSanityManager(context.getSource().getPlayer());
						manager.isActive = false;
						manager.updateThis();
						return 0;
					}))
				.then(literal("activate")
					.executes(context -> {
						SanityManager manager = getSanityManager(context.getSource().getPlayer());
						manager.isActive = true;
						manager.updateThis();
						return 0;
					}))
				.then(literal("set")
					.then(literal("sanityLevel")
						.then(argument("level", IntegerArgumentType.integer(0, 20)).executes(context -> {
							SanityManager manager = getSanityManager(context.getSource().getPlayer());
							int level = IntegerArgumentType.getInteger(context, "level");
							return manager.setSanityLevel(level);
						})))
					.then(literal("shouldRegen")
						.then(argument("regen", BoolArgumentType.bool()).executes(context -> {
							SanityManager manager = getSanityManager(context.getSource().getPlayer());
							return manager.setRegenCommand(BoolArgumentType.getBool(context, "regen"));
						})
					))
				)
				.then(literal("clear").executes(context -> {
					SanityManager manager = getSanityManager(context.getSource().getPlayer());
					return manager.clear();
				}))
				.then(literal("collapse")
					.then(argument("ticks", IntegerArgumentType.integer())
						.then(argument("reason", CollapsingReasonArgumentType.collapsingReason()).executes(context -> {
							SanityManager manager = getSanityManager(context.getSource().getPlayer());
							return manager.setCollapseTicks(IntegerArgumentType.getInteger(context, "ticks"), CollapsingReasonArgumentType.getReason(context, "reason"), context.getSource().getPlayer());
						}
					)))
				))
		));

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
		if (name == "void-stained_crying_block") {
			rarity = Rarity.EPIC;
			stack = 1;
		}
		if (name == "hardened_core") {
			rarity = Rarity.RARE;
			stack = 1;
		}

		settings = settings.rarity(rarity);
		settings = settings.maxCount(stack);
		
		BlockItem blockItem = new BlockItem(block, settings);
		Registry.register(Registries.ITEM, id, blockItem);

		return Registry.register(Registries.BLOCK, id, block);
	}

	public static <T extends BlockEntityType<?>> T registerBlockEntityType(String path, T blockEntityType) {
    	return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(ID, path), blockEntityType);
	}
 
    public static ArmorMaterial registerMaterial(int durability, Map<EquipmentType, Integer> defensePoints, int enchantability, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> tagKey, RegistryKey<EquipmentAsset> registr) {
        ArmorMaterial material = new ArmorMaterial(durability, defensePoints, enchantability, equipSound, toughness, knockbackResistance, tagKey, registr);
		return material;
    }
	
	public static Hand getHandThatHasCryingShield(LivingEntity entity) {
		Hand[] hands = {Hand.MAIN_HAND, Hand.OFF_HAND};
		for (Hand hand : hands) {
			ItemStack stack = entity.getStackInHand(hand);
			if (stack != null && stack.getItem() instanceof CryingShieldItem) 
				return hand;
		}
		return null;
	}

	public static Item getCoreIngredient(ItemStack stack) {
		if (stack.isEmpty())
			LOGGER.info("crying: EMPTY STACK ERROR");

		if (stack.getItem() instanceof CryingTool tool)
			return tool.getCoreIngredient();
		return CRYING_INGOT;
	}

	public static boolean isTheCriersSword(ItemStack stack) {
		if (stack  == null || stack.isEmpty())
			return false;
		return stack.getItem() instanceof CryingTool tool && tool.getToolType() == ToolType.SWORD && getCoreIngredient(stack) == OVER_HARDENED_CORE_WITH_EYE.asItem();
	}

	public static boolean isTheCryingBeing(ItemStack stack) {
		if (stack  == null || stack.isEmpty())
			return false;
		return stack.getItem() instanceof CryingTool tool && tool.getToolType() == ToolType.PICKAXE && getCoreIngredient(stack) == OVER_HARDENED_CORE_WITH_EYE.asItem();
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
		else if (item == Items.COPPER_INGOT)
			return Text.translatable("core.ingredient.copper").setStyle(Style.EMPTY.withColor(ColorHelper.getArgb(255, 255, 128, 0)));
		else if (item == OVER_HARDENED_CORE_WITH_EYE.asItem())
			return Text.translatable("item.crying.over-hardened_core_with_eye").setStyle(Style.EMPTY.withColor(1966200));

		return Text.translatable("core.ingredient.crying").setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE));
	}

	public static MutableText getInfusionItemText(ItemStack stack) {
		if (stack.getItem() instanceof AbstractCryingIngotItem ingot) {
			Item item = ingot.getInfusedItem();
			if (item == Items.IRON_INGOT)
				return Text.translatable("core.ingredient.iron").setStyle(Style.EMPTY.withColor(Formatting.GRAY));
			else if (item == Items.DIAMOND)
				return Text.translatable("core.ingredient.diamond").setStyle(Style.EMPTY.withColor(Formatting.AQUA));
			else if (item == Items.NETHERITE_INGOT)
				return Text.translatable("core.ingredient.netherite").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
			else if (item == Items.GOLD_INGOT)
				return Text.translatable("core.ingredient.gold").setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
			else if (item == Items.COPPER_INGOT)
				return Text.translatable("core.ingredient.copper").setStyle(Style.EMPTY.withColor(ColorHelper.getArgb(255, 255, 128, 0)));

			return Text.translatable("core.ingredient.nothing");
		}

		return null;
	}

	public static int nextBetweenInt(int min, int max) {
		Random random = new Random();
		int f = random.nextInt(max - min + 1) + min;
		return f;
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

	public static SanityManager getSanityManager(PlayerEntity player) {
		return ((SanityVars) (Object) player).getManager();
	} 

	public static HookVars getHook(PlayerEntity player) {
		return (HookVars) (Object) player;
	}
}