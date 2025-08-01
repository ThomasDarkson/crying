package crying.tools;

import java.util.function.Consumer;

import crying.Crying;
import crying.interfaces.CryingTool;
import crying.tools.sword.CriersSwordItem;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class CryingToolItem extends Item implements CryingTool {
    public static final int OVER_HARDENED_CORE_WITH_EYE_DURABILITY = Integer.MAX_VALUE;
    public static final int OVER_HARDENED_CORE_WITH_EYE_ENCHANTABILITY = 255;
    public static final float OVER_HARDENED_CORE_WITH_EYE_SPEED = (float) Integer.MAX_VALUE;
    public static final float OVER_HARDENED_CORE_WITH_EYE_PICKAXE_ATTACK_DAMAGE_BONUS = 7.5f;
    public static final float OVER_HARDENED_CORE_WITH_EYE_SWORD_ATTACK_DAMAGE_BONUS = 88f;

    public static final int CRYING_DURABILITY = 30121;
    public static final int CRYING_ENCHANTABILITY = 90;
    public static final float CRYING_SPEED = 266F;
    public static final int CRYING_AXE_SPEED = 120;

    public static final int NETHERITE_DURABILITY = 26357;
    public static final int NETHERITE_ENCHANTABILITY = 80;
    public static final float NETHERITE_SPEED = 206F;
    public static final int NETHERITE_AXE_SPEED = 100;

    public static final int DIAMOND_DURABILITY = 22586;
    public static final int DIAMOND_ENCHANTABILITY = 60;
    public static final float DIAMOND_SPEED = 166F;
    public static final int DIAMOND_AXE_SPEED = 80;

    public static final int IRON_DURABILITY = 18817;
    public static final int IRON_ENCHANTABILITY = 70;
    public static final float IRON_SPEED = 150F;
    public static final int IRON_AXE_SPEED = 60;

    public static final int GOLD_DURABILITY = 17315;
    public static final int GOLD_ENCHANTABILITY = 100;
    public static final float GOLD_SPEED = 320F;
    public static final int GOLD_AXE_SPEED = 140;

    public static final float CRYING_SWORD_ATTACK_DAMAGE_BONUS = 20f;
    public static final float CRYING_AXE_ATTACK_DAMAGE_BONUS = 20f * 1.25f;      
    public static final float CRYING_PICKAXE_ATTACK_DAMAGE_BONUS = 20f * 0.5f;  
    public static final float CRYING_SHOVEL_ATTACK_DAMAGE_BONUS = 20f * 0.75f; 
    public static final float CRYING_HOE_ATTACK_DAMAGE_BONUS = 20f * 0.3f;      

    public static final float NETHERITE_SWORD_ATTACK_DAMAGE_BONUS = 8f;
    public static final float NETHERITE_AXE_ATTACK_DAMAGE_BONUS = 8f * 1.25f;     
    public static final float NETHERITE_PICKAXE_ATTACK_DAMAGE_BONUS = 8f * 0.5f; 
    public static final float NETHERITE_SHOVEL_ATTACK_DAMAGE_BONUS = 8f * 0.75f;  
    public static final float NETHERITE_HOE_ATTACK_DAMAGE_BONUS = 8f * 0.3f;      

    public static final float DIAMOND_SWORD_ATTACK_DAMAGE_BONUS = 6f;
    public static final float DIAMOND_AXE_ATTACK_DAMAGE_BONUS = 6f * 1.25f;     
    public static final float DIAMOND_PICKAXE_ATTACK_DAMAGE_BONUS = 6f * 0.5f; 
    public static final float DIAMOND_SHOVEL_ATTACK_DAMAGE_BONUS = 6f * 0.75f;  
    public static final float DIAMOND_HOE_ATTACK_DAMAGE_BONUS = 6f * 0.3f;      

    public static final float IRON_SWORD_ATTACK_DAMAGE_BONUS = 5f;
    public static final float IRON_AXE_ATTACK_DAMAGE_BONUS = 5f * 1.25f;      
    public static final float IRON_PICKAXE_ATTACK_DAMAGE_BONUS = 5f * 0.5f;     
    public static final float IRON_SHOVEL_ATTACK_DAMAGE_BONUS = 5f * 0.75f;    
    public static final float IRON_HOE_ATTACK_DAMAGE_BONUS = 5f * 0.3f;      

    public static final float GOLD_SWORD_ATTACK_DAMAGE_BONUS = 4f;
    public static final float GOLD_AXE_ATTACK_DAMAGE_BONUS = 4f * 1.25f;   
    public static final float GOLD_PICKAXE_ATTACK_DAMAGE_BONUS = 4f * 0.5f;     
    public static final float GOLD_SHOVEL_ATTACK_DAMAGE_BONUS = 4f * 0.75f;     
    public static final float GOLD_HOE_ATTACK_DAMAGE_BONUS = 4f * 0.3f;


    public CryingToolItem(Settings settings) {
        super(settings.fireproof());
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        if (this instanceof CriersSwordItem)
            return;

        MutableText text = Text.translatable("core.ingredient");
        text.append(Text.literal(": "));
        text.append(Crying.getCoreIngredientText(stack));

        textConsumer.accept(text);
    }

    @Override
    public Item getCoreIngredient() {
        return null;
    }
}
