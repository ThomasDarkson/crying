package crying.tools;

import java.util.List;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class CryingUpgrade extends Item {
    public CryingUpgrade() {
        super(new Item.Settings()
        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(CryingTools.MOD_ID, "crying_upgrade_smithing_template")))
        .fireproof()
        .rarity(Rarity.RARE));
        CryingTools.register(this, "crying_upgrade_smithing_template");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, this));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        MutableText ingredient = Text.translatable("item.minecraft.smithing_template.ingredients");
        ingredient.getSiblings().addFirst(Text.of(" "));
        ingredient.formatted(Formatting.GRAY);

        tooltip.add(Text.translatable("item.minecraft.smithing_template").formatted(Formatting.GRAY));
        tooltip.add(Text.of(""));
        tooltip.add(Text.translatable("item.minecraft.smithing_template.applies_to").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.crying.netherite_equipment").formatted(Formatting.BLUE)); 
        tooltip.add(ingredient); 
        tooltip.add(Text.translatable("item.crying.crying_ingot_space").formatted(Formatting.BLUE));
    }
}
