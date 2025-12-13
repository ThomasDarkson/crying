package crying.tools.abstracts.copper;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import crying.enums.ToolType;
import crying.interfaces.OxidizableCryingTool;
import crying.other.CryingTags;
import crying.tools.CryingToolItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

public class AbstractCryingAxeCopperItem extends AxeItem implements OxidizableCryingTool {
    public AbstractCryingAxeCopperItem(int durability, float speed, int enchantable, String id) {
        super(new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, durability, speed, CryingToolItem.AXE_ATTACK_DAMAGE_BONUS, enchantable, CryingTags.CryingTag), 10F, -3F, new Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Crying.ID, id)))
        .component(Crying.WAS_WAXED, false)
        .component(Crying.OXIDATION_SECONDS, 0)
        .component(Crying.OXIDATION_LEVEL, WeatherState.UNAFFECTED.getSerializedName())
        .fireResistant());

        Crying.register(this, id);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.NETHERITE_AXE, this));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        MutableComponent text = Component.translatable("core.ingredient");
        text.append(Component.literal(": "));
        text.append(Crying.getCoreIngredientText(stack));
        
        textConsumer.accept(text);
    }

    @Override
    public ToolType getToolType() {
        return ToolType.AXE;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);

        this.tickInventory(stack, world, entity, slot);
    }

    @Override
    public Item getCoreIngredient() {
        return Items.COPPER_INGOT;
    }

    @Override
    public Component getName(ItemStack stack) {
        MutableComponent text = this.getOxidizedName(stack);
        if (text != null)
            return text.append(Component.literal(" ")).append(Component.translatable("item.crying.crying_axe"));

        return Component.translatable("item.crying.crying_axe");
    }
}
