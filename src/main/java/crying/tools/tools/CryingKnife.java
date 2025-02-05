package crying.tools.tools;


import crying.tools.Crying;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
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

public class CryingKnife extends SwordItem {
    private static int durability = 6498;

    private static float speed = 5F;

    private static float attackDamageBonus = 0F;

    private static int enchantability = 50;

    public CryingKnife() {
        super(new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, durability, speed, attackDamageBonus, enchantability, ItemTags.IRON_TOOL_MATERIALS), 11F, -1.2F, new Item.Settings()
        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Crying.MOD_ID, "crying_knife")))
        .enchantable(enchantability));

        Crying.register(this, "crying_knife");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.addAfter(Crying.knife, this));
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return true;
    }

    @Override
    public Text getName(ItemStack stack) {
        int damage = (stack.getComponents().get(DataComponentTypes.DAMAGE));
        if (damage >= (4332)) {
            return Text.translatable("item.crying.crying_knife_heavily_damaged");
        }
        if (damage >= (2166))
        {
            return Text.translatable("item.crying.crying_knife_damaged");
        }
        return super.getName(stack);
    }
}
