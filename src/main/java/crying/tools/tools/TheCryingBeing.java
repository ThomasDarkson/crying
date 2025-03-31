package crying.tools.tools;

import java.util.Random;
import java.util.function.Consumer;

import crying.tools.Crying;
import crying.tools.other.CryingTags;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class TheCryingBeing extends HoeItem {
    public TheCryingBeing() {
        super(new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, Integer.MAX_VALUE, ((float) Integer.MAX_VALUE), 0f, Integer.MAX_VALUE, CryingTags.EMPTY), 2147483647F, 614F, new Item.Settings()
        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, "the_crying_being")))
        .fireproof()
        .rarity(Rarity.EPIC)
        .enchantable(Integer.MAX_VALUE));

        Crying.register(this, "the_crying_being");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.MACE, this));
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Random r = new Random();
        int low = 0;
        int high = 5;
        int result = r.nextInt(high - low) + low;

        String[] literals = {
            "618",
            "...",
            "V2h5Pw==",
            "MTE=",
            "TGl2aW5nIE1pY2U=",
            ""
        };

        textConsumer.accept(Text.literal(literals[result]).fillStyle(Style.EMPTY.withObfuscated(true)));
    }
}