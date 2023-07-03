package fuzs.essentialpotions.client.handler;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.helper.AlchemyBagHelper;
import fuzs.essentialpotions.network.ServerboundCyclePotionMessage;
import fuzs.essentialpotions.world.item.ForwardingItem;
import fuzs.puzzlesapi.api.client.slotcycling.v1.ItemCyclingProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record ForwardingCyclingProvider(ItemStack itemInHand,
                                        InteractionHand interactionHand) implements ItemCyclingProvider {

    private static ItemStack getFilledStack(ItemStack itemInHand, boolean forward) {
        return AlchemyBagHelper.getContainer(itemInHand).map(container -> {
            return container.getItem(AlchemyBagHelper.findFilledSlot(itemInHand, container, forward));
        }).orElse(ItemStack.EMPTY);
    }

    private static boolean performItemCycling(ItemStack itemInHand, InteractionHand interactionHand, int slot, boolean forward) {
        if (slot != -1) {
            itemInHand.getTag().putInt(ForwardingItem.TAG_SELECTED, slot);
            EssentialPotions.NETWORK.sendToServer(new ServerboundCyclePotionMessage(interactionHand, slot, forward));
            ForwardingItemCyclingHandler.setLastToolHighlight(((ForwardingItem) itemInHand.getItem()).getSelectedItem(itemInHand));
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getSelectedStack() {
        return AlchemyBagHelper.getContainer(this.itemInHand).map(container -> {
            int selected = this.itemInHand.getTag().getInt(ForwardingItem.TAG_SELECTED);
            return container.getItem(selected);
        }).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getForwardStack() {
        return getFilledStack(this.itemInHand, true);
    }

    @Override
    public ItemStack getBackwardStack() {
        return getFilledStack(this.itemInHand, false);
    }

    @Override
    public int getForwardSlot() {
        return AlchemyBagHelper.getForwardSlot(this.itemInHand);
    }

    @Override
    public int getBackwardSlot() {
        return AlchemyBagHelper.getBackwardSlot(this.itemInHand);
    }

    @Override
    public boolean cycleSlotForward() {
        return performItemCycling(this.itemInHand, this.interactionHand, this.getForwardSlot(), true);
    }

    @Override
    public boolean cycleSlotBackward() {
        return performItemCycling(this.itemInHand, this.interactionHand, this.getBackwardSlot(), false);
    }
}
