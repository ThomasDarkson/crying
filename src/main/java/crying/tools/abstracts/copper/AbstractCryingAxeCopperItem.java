package crying.tools.abstracts.copper;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import crying.enums.ToolType;
import crying.interfaces.OxidizableCryingTool;
import crying.other.CryingTags;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Oxidizable.OxidationLevel;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbstractCryingAxeCopperItem extends AxeItem implements OxidizableCryingTool {
    public AbstractCryingAxeCopperItem(int durability, float speed, float bonus, int enchantable, String id) {
        super(new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, bonus, enchantable, CryingTags.CryingTag), 10F, -3F, new Item.Settings()
        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.ID, id)))
        .component(Crying.WAS_WAXED, false)
        .component(Crying.OXIDATION_SECONDS, 0)
        .component(Crying.OXIDATION_LEVEL, OxidationLevel.UNAFFECTED.asString())
        .fireproof());

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_AXE, this));
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        MutableText text = Text.translatable("core.ingredient");
        text.append(Text.literal(": "));
        text.append(Crying.getCoreIngredientText(stack));
        
        textConsumer.accept(text);
    }

    @Override
    public ToolType getToolType() {
        return ToolType.AXE;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);

        this.tickInventory(stack, world, entity, slot);
    }

    @Override
    public Item getCoreIngredient() {
        return Items.COPPER_INGOT;
    }

    @Override
    public Text getName(ItemStack stack) {
        MutableText text = this.getOxidizedName(stack);
        if (text != null)
            return text.append(Text.literal(" ")).append(Text.translatable("item.crying.crying_axe"));

        return Text.translatable("item.crying.crying_axe");
    }
}
