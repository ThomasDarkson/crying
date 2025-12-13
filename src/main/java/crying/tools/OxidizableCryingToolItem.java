package crying.tools;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import crying.interfaces.OxidizableCryingTool;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

public class OxidizableCryingToolItem extends CryingToolItem implements OxidizableCryingTool {
    public OxidizableCryingToolItem(Properties settings) {
        super(settings.component(Crying.WAS_WAXED, false).component(Crying.OXIDATION_SECONDS, 0).component(Crying.OXIDATION_LEVEL, WeatherState.UNAFFECTED.getSerializedName()));
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
            return text.append(Component.literal(" ")).append(super.getName(stack));

        return super.getName(stack);
    }
}
