package crying.other;

import java.util.concurrent.CompletableFuture;

import crying.Crying;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CryingTags extends FabricTagProvider<Item> {
    public static final TagKey<Item> CryingTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_repair"));

    public static final TagKey<Item> AXES_AND_SWORDS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "axes_and_swords"));

    public static final TagKey<Item> CRYING_AXES = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_axes"));
    public static final TagKey<Item> CRYING_PICKAXES = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_pickaxes"));
    public static final TagKey<Item> CRYING_SWORDS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_swords"));
    public static final TagKey<Item> CRYING_SHOVELS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_shovels"));
    public static final TagKey<Item> CRYING_HOES = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, "crying_hoes"));

    public CryingTags(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ITEM, registriesFuture);
    }

    public static void initialize() {
    }

    @Override
    protected void addTags(Provider wrapperLookup) {
    }
}
