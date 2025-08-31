package crying.backend;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import crying.enums.CollapsingReason;
import net.minecraft.command.CommandSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CollapsingReasonArgumentType implements ArgumentType<CollapsingReason> {
    private CollapsingReasonArgumentType() {
    }

    public static CollapsingReasonArgumentType collapsingReason() {
        return new CollapsingReasonArgumentType();
    }

    @Override
    public CollapsingReason parse(StringReader reader) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        CollapsingReason reason = CollapsingReason.fromString(input);
        return reason;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(Arrays.stream(CollapsingReason.values()).map(CollapsingReason::getName),  builder);
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.stream(CollapsingReason.values()).map(CollapsingReason::getName).collect(Collectors.toList());
    }

    public static CollapsingReason getReason(CommandContext<?> context, String name) {
        return context.getArgument(name, CollapsingReason.class);
    }
}
