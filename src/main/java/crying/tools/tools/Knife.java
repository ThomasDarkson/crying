package crying.tools.tools;


import crying.tools.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Knife extends SwordItem {
    private static int durability = 150;

    private static float speed = 5F;

    private static float attackDamageBonus = 0F;

    private static int enchantability = 14;

    public Knife() {
        super(new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, durability, speed, attackDamageBonus, enchantability, ItemTags.IRON_TOOL_MATERIALS), 2F, -1.7F, new Item.Settings()
        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.MOD_ID, "knife")))
        .enchantable(enchantability));

        Crying.register(this, "knife");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Items.IRON_SWORD, this));
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return true;
    }

    @Override
    public Text getName(ItemStack stack) {
        int damage = stack.getComponents().get(DataComponentTypes.DAMAGE);
        if (damage >= 100) {
            return Text.translatable("item.crying.knife_heavily_damaged");
        }
        if (damage >= 50)
        {
            return Text.translatable("item.crying.knife_damaged");
        }
        return super.getName(stack);
    }
}
