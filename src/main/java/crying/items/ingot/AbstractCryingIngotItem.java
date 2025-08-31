package crying.items.ingot;

import java.util.function.Consumer;

import crying.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbstractCryingIngotItem extends Item {
    public AbstractCryingIngotItem(String id) {
        super(new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, id)))
            .fireproof());

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_INGOT, this));
    }

    @Override
    public Text getName(ItemStack stack) {
        MutableText text = Text.translatable("item.crying.crying_ingot");
        if (getInfusedItem() == null)
            return Text.translatable("core.ingredient.uninfused").append(" ").append(text);
        return text;
    }

    @Override    
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        if (getInfusedItem() != null) {
            MutableText text = Text.translatable("crying.infused.with");
            text.append(Text.literal(": "));
            text.append(Crying.getInfusionItemText(stack));
            textConsumer.accept(text);
        }
    }

    public Item getInfusedItem() {
        return null;
    }
}
