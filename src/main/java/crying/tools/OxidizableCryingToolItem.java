package crying.tools;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import crying.interfaces.OxidizableCryingTool;
import net.minecraft.block.Oxidizable.OxidationLevel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class OxidizableCryingToolItem extends CryingToolItem implements OxidizableCryingTool {
    public OxidizableCryingToolItem(Settings settings) {
        super(settings.component(Crying.WAS_WAXED, false).component(Crying.OXIDATION_SECONDS, 0).component(Crying.OXIDATION_LEVEL, OxidationLevel.UNAFFECTED.asString()));
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
            return text.append(Text.literal(" ")).append(super.getName(stack));

        return super.getName(stack);
    }
}
