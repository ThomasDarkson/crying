package crying.tools.hoe;

import java.util.function.Consumer;

import crying.Crying;
import crying.interfaces.CryingTool;
import crying.other.CryingTags;
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
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbstractCryingHoeItem extends HoeItem implements CryingTool {
    public AbstractCryingHoeItem(int durability, float speed, float bonus, int enchantable, String id) {
        super(new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, bonus, enchantable, CryingTags.CryingTag), 0F, +0F, new Item.Settings()
        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, id)))
        .fireproof());

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_HOE, this));
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        MutableText text = Text.translatable("core.ingredient");
        text.append(Text.literal(": "));
        text.append(Crying.getCoreIngredientText(stack));

        textConsumer.accept(text);
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable("item.crying.crying_hoe");
    }

    @Override
    public Item getCoreIngredient() {
        return null;
    }
}
