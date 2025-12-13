package crying.tools.abstracts;

import java.util.function.Consumer;

import crying.Crying;
import crying.enums.ToolType;
import crying.interfaces.CryingTool;
import crying.other.CryingTags;
import crying.tools.CryingToolItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class AbstractCryingHoeItem extends HoeItem implements CryingTool {
    public AbstractCryingHoeItem(int durability, float speed, int enchantable, String id) {
        super(new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, CryingToolItem.HOE_ATTACK_DAMAGE_BONUS, enchantable, CryingTags.CryingTag), 0F, +0F, new Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, id)))
        .fireResistant());

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_HOE, this));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        MutableComponent text = Component.translatable("core.ingredient");
        text.append(Component.literal(": "));
        text.append(Crying.getCoreIngredientText(stack));

        textConsumer.accept(text);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.crying.crying_hoe");
    }

    @Override
    public Item getCoreIngredient() {
        return null;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.HOE;
    }
}
