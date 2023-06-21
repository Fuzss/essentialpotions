package fuzs.essentialpotions.client.handler;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.helper.AlchemyBagHelper;
import fuzs.essentialpotions.network.ServerboundCyclePotionMessage;
import fuzs.essentialpotions.world.item.AlchemyBagItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record AlchemyBagCyclingProvider(ItemStack itemInHand, InteractionHand interactionHand) implements ItemCyclingProvider {

    @Override
    public ItemStack getSelectedStack() {
        return AlchemyBagHelper.getContainer(this.itemInHand()).map(container -> {
            int selected = this.itemInHand().getTag().getInt(AlchemyBagItem.TAG_SELECTED);
            return container.getItem(selected);
        }).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getForwardStack() {
        return AlchemyBagHelper.getContainer(this.itemInHand()).map(container -> {
            return container.getItem(AlchemyBagHelper.findEmptySlot(this.itemInHand(), container, true));
        }).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getBackwardStack() {
        return AlchemyBagHelper.getContainer(this.itemInHand()).map(container -> {
            return container.getItem(AlchemyBagHelper.findEmptySlot(this.itemInHand(), container, false));
        }).orElse(ItemStack.EMPTY);
    }

    @Override
    public int getForwardSlot() {
        return AlchemyBagHelper.getForwardSlot(this.itemInHand());
    }

    @Override
    public int getBackwardSlot() {
        return AlchemyBagHelper.getBackwardSlot(this.itemInHand());
    }

    @Override
    public boolean cycleSlotForward() {
        if (this.getForwardSlot() != -1) {
            this.performItemCycling(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean cycleSlotBackward() {
        if (this.getBackwardSlot() != -1) {
            this.performItemCycling(false);
            return true;
        }
        return false;
    }

    private void performItemCycling(boolean forward) {
        int slot = forward ? this.getForwardSlot() : this.getBackwardSlot();
        if (slot != -1) {
            this.itemInHand.getTag().putInt(AlchemyBagItem.TAG_SELECTED, slot);
            EssentialPotions.NETWORK.sendToServer(new ServerboundCyclePotionMessage(this.interactionHand(), slot, forward));
            ForwardingItemCyclingHandler.toolHighlightTimer = 40;
            ForwardingItemCyclingHandler.lastToolHighlight = ((AlchemyBagItem) this.itemInHand.getItem()).getSelectedItem(this.itemInHand);
        }
    }
}
