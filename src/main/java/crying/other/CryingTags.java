package crying.other;

import java.util.concurrent.CompletableFuture;

import crying.Crying;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CryingTags extends FabricTagProvider<Item> {
    public static final TagKey<Item> CryingTag = TagKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_repair"));

    public static final TagKey<Item> EMPTY = TagKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "empty"));

    public static final TagKey<Item> AXES_AND_SWORDS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "axes_and_swords"));

    public static final TagKey<Item> CRYING_AXES = TagKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_axes"));
    public static final TagKey<Item> CRYING_PICKAXES = TagKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_axes"));
    public static final TagKey<Item> CRYING_SWORDS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_axes"));
    public static final TagKey<Item> CRYING_SHOVELS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_axes"));
    public static final TagKey<Item> CRYING_HOES = TagKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "crying_axes"));

    public CryingTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ITEM, registriesFuture);
    }

    public static void initialize() {
    }

    @Override
    protected void configure(WrapperLookup wrapperLookup) {
    }
}
