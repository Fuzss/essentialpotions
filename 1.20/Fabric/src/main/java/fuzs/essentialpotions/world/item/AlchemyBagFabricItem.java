package fuzs.essentialpotions.world.item;

import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class AlchemyBagFabricItem extends AlchemyBagItem implements FabricItem {

    public AlchemyBagFabricItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean allowNbtUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return !ItemStack.isSameItem(oldStack, newStack) || !ItemStack.matches(this.getSelectedItem(oldStack), this.getSelectedItem(newStack));
    }

    @Override
    public boolean isSuitableFor(ItemStack stack, BlockState state) {
        ItemStack item = this.getSelectedItem(stack);
        return !item.isEmpty() ? item.isCorrectToolForDrops(state) : super.isSuitableFor(stack, state);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        ItemStack item = this.getSelectedItem(stack);
        return !item.isEmpty() ? item.getAttributeModifiers(slot) : super.getAttributeModifiers(stack, slot);
    }
}
