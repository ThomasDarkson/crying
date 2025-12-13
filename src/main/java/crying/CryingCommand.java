package crying;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import crying.armors.CryingArmor;
import crying.backend.CollapsingReasonArgumentType;
import crying.enums.CollapsingReason;
import crying.interfaces.SanityManager;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import static net.minecraft.commands.Commands.*;

public class CryingCommand {
    public static void init() {
        ArgumentTypeRegistry.registerArgumentType(Identifier.fromNamespaceAndPath(Crying.ID, "collapsing_reason"), CollapsingReasonArgumentType.class, SingletonArgumentInfo.contextFree(CollapsingReasonArgumentType::collapsingReason));
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("crying")
			.executes(context -> {
				context.getSource().sendSuccess(() -> Component.literal("Crying Tools ").append(Crying.VERSION.toString()), false);
				context.getSource().sendSuccess(() -> Component.translatable("crying.thank.you"), false);
				return 0;
			})
			.then(literal("sanity").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(literal("deactivate")
					.executes(context -> {
						warnPlayer(context);

						SanityManager manager = Crying.getSanityManager(context.getSource().getPlayer());
                        if (!manager.isActive) {
                            context.getSource().sendSuccess(() -> Component.translatable("crying.command.alreadyInactive").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                            return 1;
                        }
						manager.isActive = false;
						manager.updateThis();
                        context.getSource().sendSuccess(() -> Component.translatable("crying.command.deactivate"), false);
						return 0;
					}))
				.then(literal("activate")
					.executes(context -> {
						warnPlayer(context);

						SanityManager manager = Crying.getSanityManager(context.getSource().getPlayer());
                        if (manager.isActive) {
                            context.getSource().sendSuccess(() -> Component.translatable("crying.command.alreadyActive").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                            return 1;
                        }
						manager.isActive = true;
						manager.updateThis();
                        context.getSource().sendSuccess(() -> Component.translatable("crying.command.active"), false);
						return 0;
					}))
				.then(literal("set")
					.then(literal("sanityLevel")
						.then(argument("level", IntegerArgumentType.integer(0, 20)).executes(context -> {
							warnPlayer(context);

							SanityManager manager = Crying.getSanityManager(context.getSource().getPlayer());
							int level = IntegerArgumentType.getInteger(context, "level");
                            context.getSource().sendSuccess(() -> Component.translatable("crying.command.level", "" + level), false);
							return manager.setSanityLevel(level);
						})))
					.then(literal("shouldRegen")
						.then(argument("regen", BoolArgumentType.bool()).executes(context -> {
							warnPlayer(context);

							SanityManager manager = Crying.getSanityManager(context.getSource().getPlayer());
                            boolean shouldRegen = BoolArgumentType.getBool(context, "regen");
                            if (shouldRegen)
                                context.getSource().sendSuccess(() -> Component.translatable("crying.command.regen"), false);
                            else
                                context.getSource().sendSuccess(() -> Component.translatable("crying.command.noRegen"), false);
							return manager.setRegenCommand(shouldRegen);
						})
					))
				)
				.then(literal("clear").executes(context -> {
					warnPlayer(context);

					SanityManager manager = Crying.getSanityManager(context.getSource().getPlayer());
                    context.getSource().sendSuccess(() -> Component.translatable("crying.command.clear"), false);
					return manager.clear();
				}))
				.then(literal("collapse")
					.then(argument("ticks", IntegerArgumentType.integer())
						.then(argument("reason", CollapsingReasonArgumentType.collapsingReason()).executes(context -> {
							warnPlayer(context);

							SanityManager manager = Crying.getSanityManager(context.getSource().getPlayer());
                            int ticks = IntegerArgumentType.getInteger(context, "ticks");
                            CollapsingReason reason = CollapsingReasonArgumentType.getReason(context, "reason");
                            context.getSource().sendSuccess(() -> Component.translatable("crying.command.collapse", "" + ticks, reason.getTranslatableName()), false);
							return manager.setCollapseTicks(ticks, reason, context.getSource().getPlayer());
						}
					)))
				))
		));
    }

	private static void warnPlayer(CommandContext<CommandSourceStack> context) {
		ServerPlayer player = context.getSource().getPlayer();
		if (player != null) {
			int count = CryingArmor.cryingArmorCount(player);
			if (count == 0) {
				context.getSource().sendSuccess(() -> Component.translatable("crying.command.warn").setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)), false);
			}
		}
	}
}
