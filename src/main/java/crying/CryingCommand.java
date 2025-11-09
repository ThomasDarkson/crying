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
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import static net.minecraft.server.command.CommandManager.*;

public class CryingCommand {
    public static void init() {
        ArgumentTypeRegistry.registerArgumentType(Identifier.of(Crying.ID, "collapsing_reason"), CollapsingReasonArgumentType.class, ConstantArgumentSerializer.of(CollapsingReasonArgumentType::collapsingReason));
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("crying")
			.executes(context -> {
				context.getSource().sendFeedback(() -> Text.literal("Crying Tools ").append(Crying.VERSION.toString()), false);
				context.getSource().sendFeedback(() -> Text.translatable("crying.thank.you"), false);
				return 0;
			})
			.then(literal("sanity").requires(source -> source.hasPermissionLevel(2))
				.then(literal("deactivate")
					.executes(context -> {
						warnPlayer(context);

						SanityManager manager = Crying.getSanityManager(context.getSource().getPlayer());
                        if (!manager.isActive) {
                            context.getSource().sendFeedback(() -> Text.translatable("crying.command.alreadyInactive").setStyle(Style.EMPTY.withColor(Formatting.RED)), false);
                            return 1;
                        }
						manager.isActive = false;
						manager.updateThis();
                        context.getSource().sendFeedback(() -> Text.translatable("crying.command.deactivate"), false);
						return 0;
					}))
				.then(literal("activate")
					.executes(context -> {
						warnPlayer(context);

						SanityManager manager = Crying.getSanityManager(context.getSource().getPlayer());
                        if (manager.isActive) {
                            context.getSource().sendFeedback(() -> Text.translatable("crying.command.alreadyActive").setStyle(Style.EMPTY.withColor(Formatting.RED)), false);
                            return 1;
                        }
						manager.isActive = true;
						manager.updateThis();
                        context.getSource().sendFeedback(() -> Text.translatable("crying.command.active"), false);
						return 0;
					}))
				.then(literal("set")
					.then(literal("sanityLevel")
						.then(argument("level", IntegerArgumentType.integer(0, 20)).executes(context -> {
							warnPlayer(context);

							SanityManager manager = Crying.getSanityManager(context.getSource().getPlayer());
							int level = IntegerArgumentType.getInteger(context, "level");
                            context.getSource().sendFeedback(() -> Text.translatable("crying.command.level", "" + level), false);
							return manager.setSanityLevel(level);
						})))
					.then(literal("shouldRegen")
						.then(argument("regen", BoolArgumentType.bool()).executes(context -> {
							warnPlayer(context);

							SanityManager manager = Crying.getSanityManager(context.getSource().getPlayer());
                            boolean shouldRegen = BoolArgumentType.getBool(context, "regen");
                            if (shouldRegen)
                                context.getSource().sendFeedback(() -> Text.translatable("crying.command.regen"), false);
                            else
                                context.getSource().sendFeedback(() -> Text.translatable("crying.command.noRegen"), false);
							return manager.setRegenCommand(shouldRegen);
						})
					))
				)
				.then(literal("clear").executes(context -> {
					warnPlayer(context);

					SanityManager manager = Crying.getSanityManager(context.getSource().getPlayer());
                    context.getSource().sendFeedback(() -> Text.translatable("crying.command.clear"), false);
					return manager.clear();
				}))
				.then(literal("collapse")
					.then(argument("ticks", IntegerArgumentType.integer())
						.then(argument("reason", CollapsingReasonArgumentType.collapsingReason()).executes(context -> {
							warnPlayer(context);

							SanityManager manager = Crying.getSanityManager(context.getSource().getPlayer());
                            int ticks = IntegerArgumentType.getInteger(context, "ticks");
                            CollapsingReason reason = CollapsingReasonArgumentType.getReason(context, "reason");
                            context.getSource().sendFeedback(() -> Text.translatable("crying.command.collapse", "" + ticks, reason.getTranslatableName()), false);
							return manager.setCollapseTicks(ticks, reason, context.getSource().getPlayer());
						}
					)))
				))
		));
    }

	private static void warnPlayer(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity player = context.getSource().getPlayer();
		if (player != null) {
			int count = CryingArmor.cryingArmorCount(player);
			if (count == 0) {
				context.getSource().sendFeedback(() -> Text.translatable("crying.command.warn").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)), false);
			}
		}
	}
}
