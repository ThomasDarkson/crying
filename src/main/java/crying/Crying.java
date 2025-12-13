package crying;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.mojang.serialization.Codec;

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
import crying.interfaces.HasUniqueItemSettings;
import crying.interfaces.HookVars;
import crying.interfaces.SanityVars;
import crying.interfaces.SanityManager;
import crying.items.CrierSpawnEggItem;
import crying.items.CryingAppleItem;
import crying.items.CryingCarrotItem;
import crying.items.CryingResidueItem;
import crying.items.EnchantedCryingAppleItem;
import crying.items.EvilGranterItem;
import crying.items.EyeConnectedToAStickItem;
import crying.items.EyeItem;
import crying.items.GranterItem;
import crying.items.CryingGrapplingHookItem;
import crying.items.CryingGrapplingHookTipItem;
import crying.items.CryingIngotItem;
import crying.items.HardenedCorePieceItem;
import crying.items.LostCrierSpawnEggItem;
import crying.other.CryingLoot;
import crying.other.CryingTags;
import crying.tools.CryingShieldItem;
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
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import semantic.ver.lib.SemanticVerLib;
import semantic.ver.lib.SemanticVersion;

public class Crying implements ModInitializer {
	private static final String VERSION_URL = "https://raw.githubusercontent.com/ThomasDarkson/crying/refs/heads/version/version.txt";
	public static final SemanticVersion VERSION = SemanticVersion.stable(6, 1, 8, VERSION_URL);
    public static final String ID = "crying";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final int MAX_CRYING_FOOD_COUNT = 9888;

	public static final EntityDataAccessor<Integer> FOOD_COUNT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

	public static final EntityType<CrierEntity> CRIER = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "crier")),
		EntityType.Builder.of(CrierEntity::new, MobCategory.MONSTER).sized(0.58F, 1.98F).eyeHeight(1.75F).build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "crier")))
	);

	public static final EntityType<LostCrierEntity> LOST_CRIER = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "lost_crier")),
		EntityType.Builder.of(LostCrierEntity::new, MobCategory.CREATURE).sized(0.58F, 1.98F).eyeHeight(1.75F).build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "lost_crier")))
	);

	public static final EntityType<GrapplingHookEntity> GRAPPLING_HOOK = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "grappling_hook_entity")),
        EntityType.Builder.of(GrapplingHookEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(8).updateInterval(1).build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "grappling_hook_entity")))
    );

	public static final EntityType<GranterEntity> GRANTER_ENTITY = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "granter")),
		EntityType.Builder.of(GranterEntity::new, MobCategory.MISC).sized(0.2F, 0.2F).updateInterval(1).build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "granter")))
	);

	public static final EntityType<EvilGranterEntity> EVIL_GRANTER_ENTITY = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "evil_granter")),
		EntityType.Builder.of(EvilGranterEntity::new, MobCategory.MISC).sized(0.2F, 0.2F).updateInterval(1).build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "evil_granter")))
	);

	public static final Block CRIERS_HEART = new CriersHeartBlock(Properties.of().
            setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Crying.ID, "criers_heart"))).
            strength(0F, 36000000.0F).
            lightLevel((state) -> {
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

	public static final ResourceKey<Biome> SOMEWHAT_WEIRD_ISLAND = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(ID, "somewhat_weird_island"));
	public static final ResourceKey<Biome> SOMEWHAT_NORMAL_ISLAND = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(ID, "somewhat_normal_island"));
	public static final ResourceKey<Level> CRYING_WORLD = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(ID, "crying"));
	public static final ResourceKey<LevelStem> CRYING_DIMENSION = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(ID, "crying"));
	public static final ResourceKey<DimensionType> CRYING_DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "crying"));

	public static final DataComponentType<Boolean> WAS_WAXED;
	public static final DataComponentType<Integer> OXIDATION_SECONDS;
	public static final DataComponentType<String> OXIDATION_LEVEL;
	public static final DataComponentType<Boolean> THROWN;
	public static final DataComponentType<String> HOOK_UUID;

	public static final Holder<Potion> CRYING_POTION;
	public static final Holder<Potion> LONG_CRYING_POTION;
	
	public static final Item CRYING_SHIELD;

	public static final Item CRYING_HELMET;
	public static final Item CRYING_CHESTPLATE;
	public static final Item CRYING_CHESTPLATE_WITH_ELYTRA;
	public static final Item CRYING_LEGGINGS;
	public static final Item CRYING_BOOTS;
	public static final Item CRYING_HORSE_ARMOR;

	public static final Item GRANTER;
	public static final Item EVIL_GRANTER;

	public static final Item CRYING_GRAPPLING_HOOK;
	public static final Item CRYING_GRAPPLING_HOOK_TIP;

	public static final Item CRYING_RESIDUE;
	public static final Item CRYING_INGOT;

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

	public static final ResourceLocation CRIER_IDLE = ResourceLocation.fromNamespaceAndPath(ID, "crier_idle");
    public static final SoundEvent CRIER_IDLE_EVENT = SoundEvent.createVariableRangeEvent(CRIER_IDLE);

	public static final ResourceLocation CRIER_HURT = ResourceLocation.fromNamespaceAndPath(ID, "crier_hurt");
    public static final SoundEvent CRIER_HURT_EVENT = SoundEvent.createVariableRangeEvent(CRIER_HURT);

	public static final ResourceLocation CRIER_DIES = ResourceLocation.fromNamespaceAndPath(ID, "crier_dies");
    public static final SoundEvent CRIER_DIES_EVENT = SoundEvent.createVariableRangeEvent(CRIER_DIES);

	public static final ResourceLocation CRIER_SCREAM = ResourceLocation.fromNamespaceAndPath(ID, "crier_scream");
    public static final SoundEvent CRIER_SCREAM_EVENT = SoundEvent.createVariableRangeEvent(CRIER_SCREAM);

	public static final ResourceLocation GRANTER_HEAL = ResourceLocation.fromNamespaceAndPath(ID, "granter_heal");
	public static final SoundEvent GRANTER_HEAL_EVENT = SoundEvent.createVariableRangeEvent(GRANTER_HEAL);

	public static final ResourceLocation EVIL_GRANTER_TOUCH = ResourceLocation.fromNamespaceAndPath(ID, "evil_granter_touch");
	public static final SoundEvent EVIL_GRANTER_HEAL_TOUCH = SoundEvent.createVariableRangeEvent(EVIL_GRANTER_TOUCH);

	public static final Reference<SoundEvent> LIVING_MICE = Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(ID, "living_mice_crying_biome"), SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ID, "living_mice_crying_biome")));
	public static final Reference<SoundEvent> CRYING_MICE = Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(ID, "crying_mice"), SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ID, "crying_mice")));

	static {
		WAS_WAXED = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "was_waxed"), DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
		OXIDATION_SECONDS = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "oxidation_seconds"), DataComponentType.<Integer>builder().persistent(Codec.intRange(0, Integer.MAX_VALUE)).build());
		OXIDATION_LEVEL = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "oxidation_level"), DataComponentType.<String>builder().persistent(Codec.string(0, Integer.MAX_VALUE)).build());
		THROWN = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "hook_thrown"), DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
		HOOK_UUID = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(ID, "hook_uuid"), DataComponentType.<String>builder().persistent(Codec.string(0, Integer.MAX_VALUE)).build());
		
		CRYING_POTION = registerPotion("bane_of_criers",
            new Potion("bane_of_criers", new MobEffectInstance(BaneOfCriers.BANE_OF_CRIERS, Crying.tickSecond(120), 0)));

		LONG_CRYING_POTION = registerPotion("long_bane_of_criers",
            new Potion("bane_of_criers", new MobEffectInstance(BaneOfCriers.BANE_OF_CRIERS, Crying.tickSecond(480), 0)));

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
		CRYING_APPLE = new CryingAppleItem();
		CRYING_CARROT = new CryingCarrotItem();
		ENCHANTED_CRYING_APPLE = new EnchantedCryingAppleItem();
		EYE = new EyeItem();
		EYE_CONNECTED_TO_A_STICK = new EyeConnectedToAStickItem();
		CRIER_SPAWN_EGG = new CrierSpawnEggItem();
		LOST_CRIER_SPAWN_EGG = new LostCrierSpawnEggItem();
		GRANTER = new GranterItem();
		EVIL_GRANTER = new EvilGranterItem();
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

		Registry.register(BuiltInRegistries.SOUND_EVENT, CRIER_IDLE, CRIER_IDLE_EVENT);
		Registry.register(BuiltInRegistries.SOUND_EVENT, CRIER_HURT, CRIER_HURT_EVENT);
		Registry.register(BuiltInRegistries.SOUND_EVENT, CRIER_DIES, CRIER_DIES_EVENT);
		Registry.register(BuiltInRegistries.SOUND_EVENT, GRANTER_HEAL, GRANTER_HEAL_EVENT);
		Registry.register(BuiltInRegistries.SOUND_EVENT, EVIL_GRANTER_TOUCH, EVIL_GRANTER_HEAL_TOUCH);

		FabricDefaultAttributeRegistry.register(CRIER, CrierEntity.createCrierAttributes());
		FabricDefaultAttributeRegistry.register(LOST_CRIER, LostCrierEntity.createLostCrierAttributes());
		
		CryingCommand.init();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
			try {
				server.getPlayerList().getPlayers().forEach(player -> {
					if (player instanceof ServerPlayer serverPlayer) {
						CryingArmor.setCount(serverPlayer);
					}
				});
			}
			catch(Exception e) {
			}
        });

		EntityElytraEvents.CUSTOM.register((entity, tick) -> {
			try {
				ItemStack mainStack = entity.getItemBySlot(EquipmentSlot.CHEST);
				return mainStack.getItem() instanceof CryingChestplateWithElytraItem;
			}
			catch(Exception e) {
				return false;
			}
		});
	}

    public static final Item register(Item item, String id) {
		ResourceLocation itemID = ResourceLocation.fromNamespaceAndPath(Crying.ID, id);
		Item registeredItem = Registry.register(BuiltInRegistries.ITEM, itemID, item);
		return registeredItem;
	}

	public static Block registerBlock(Block block, String name) {
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Crying.ID, name);

		Item.Properties settings = new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, name)));

		if (block instanceof HasUniqueItemSettings unique) {
			if (unique.isFireProof())
				settings = settings.fireResistant();

			settings = settings.rarity(unique.getRarity());
			settings = settings.stacksTo(unique.getMaxCount());
		}
		
		BlockItem blockItem = new BlockItem(block, settings);
		Registry.register(BuiltInRegistries.ITEM, id, blockItem);

		return Registry.register(BuiltInRegistries.BLOCK, id, block);
	}

	private static Holder<Potion> registerPotion(String name, Potion potion) {
		return Registry.registerForHolder(BuiltInRegistries.POTION, ResourceLocation.fromNamespaceAndPath(ID, name), potion);
	}

	public static <T extends BlockEntityType<?>> T registerBlockEntityType(String path, T blockEntityType) {
    	return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ID, path), blockEntityType);
	}
 
    public static ArmorMaterial registerMaterial(int durability, Map<ArmorType, Integer> defensePoints, int enchantability, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> tagKey, ResourceKey<EquipmentAsset> registr) {
        ArmorMaterial material = new ArmorMaterial(durability, defensePoints, enchantability, equipSound, toughness, knockbackResistance, tagKey, registr);
		return material;
    }
	
	public static InteractionHand getHandThatHasCryingShield(LivingEntity entity) {
		InteractionHand[] hands = {InteractionHand.MAIN_HAND, InteractionHand.OFF_HAND};
		for (InteractionHand hand : hands) {
			ItemStack stack = entity.getItemInHand(hand);
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

	public static MutableComponent getCoreIngredientText(ItemStack stack) {
		Item item = getCoreIngredient(stack);
		if (item == Items.IRON_INGOT)
			return Component.translatable("core.ingredient.iron").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));
		else if (item == Items.DIAMOND)
			return Component.translatable("core.ingredient.diamond").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA));
		else if (item == Items.NETHERITE_INGOT)
			return Component.translatable("core.ingredient.netherite").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY));
		else if (item == Items.GOLD_INGOT)
			return Component.translatable("core.ingredient.gold").setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW));
		else if (item == Items.COPPER_INGOT)
			return Component.translatable("core.ingredient.copper").setStyle(Style.EMPTY.withColor(ARGB.color(255, 255, 128, 0)));
		else if (item == OVER_HARDENED_CORE_WITH_EYE.asItem())
			return Component.translatable("item.crying.over-hardened_core_with_eye").setStyle(Style.EMPTY.withColor(1966200));

		return Component.translatable("core.ingredient.crying").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE));
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

	public static SanityManager getSanityManager(Player player) {
		return ((SanityVars) (Object) player).getManager();
	} 

	public static HookVars getHook(Player player) {
		return (HookVars) (Object) player;
	}
}