package crying.tools;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CryingTags extends FabricTagProvider<Item> {
    public static final TagKey<Item> CryingTag = TagKey.of(RegistryKeys.ITEM, Identifier.of(CryingTools.MOD_ID, "crying_repair"));
    public CryingTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ITEM, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup wrapperLookup) {
        //getOrCreateTagBuilder(CryingTag).add(CryingTools.ingot);
    }
}
