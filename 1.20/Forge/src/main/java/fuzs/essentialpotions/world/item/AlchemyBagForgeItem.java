package fuzs.essentialpotions.world.item;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class AlchemyBagForgeItem extends AlchemyBagItem {

    public AlchemyBagForgeItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.isSameItem(oldStack, newStack) || !ItemStack.matches(this.getSelectedItem(oldStack), this.getSelectedItem(newStack));
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        ItemStack item = this.getSelectedItem(stack);
        return !item.isEmpty() ? item.isCorrectToolForDrops(state) : super.isCorrectToolForDrops(stack, state);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ItemStack item = this.getSelectedItem(stack);
        return !item.isEmpty() ? item.getAttributeModifiers(slot) : super.getAttributeModifiers(slot, stack);
    }
}
