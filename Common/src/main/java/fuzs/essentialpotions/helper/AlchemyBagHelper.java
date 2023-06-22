package fuzs.essentialpotions.helper;

import fuzs.essentialpotions.world.inventory.ContainerItemHelper;
import fuzs.essentialpotions.world.item.ForwardingItem;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class AlchemyBagHelper {

    public static int getForwardSlot(ItemStack itemInHand) {
        return getContainer(itemInHand).map(container -> {
            return findFilledSlot(itemInHand, container, true);
        }).orElse(-1);
    }

    public static int getBackwardSlot(ItemStack itemInHand) {
        return getContainer(itemInHand).map(container -> {
            return findFilledSlot(itemInHand, container, false);
        }).orElse(-1);
    }

    public static Optional<SimpleContainer> getContainer(ItemStack itemInHand) {
        return itemInHand.hasTag() ? Optional.of(ContainerItemHelper.loadItemContainer(itemInHand, null, false)) : Optional.empty();
    }

    public static int findFilledSlot(ItemStack itemInHand, SimpleContainer container, boolean forward) {
        int selected = itemInHand.getTag().getInt(ForwardingItem.TAG_SELECTED);
        for (int i = 1; i < container.getContainerSize(); i++) {
            int slot = (selected + i * (forward ? 1 : -1) + container.getContainerSize()) % container.getContainerSize();
            if (!container.getItem(slot).isEmpty()) {
                return slot;
            }
        }
        return -1;
    }
}
