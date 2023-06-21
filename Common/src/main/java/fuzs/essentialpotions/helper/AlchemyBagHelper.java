package fuzs.essentialpotions.helper;

import fuzs.essentialpotions.world.inventory.AlchemyBagMenu;
import fuzs.essentialpotions.world.inventory.ContainerItemHelper;
import fuzs.essentialpotions.world.item.AlchemyBagItem;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class AlchemyBagHelper {

    public static int getForwardSlot(ItemStack itemInHand) {
        return getContainer(itemInHand).map(container -> {
            return findEmptySlot(itemInHand, container, true);
        }).orElse(-1);
    }

    public static int getBackwardSlot(ItemStack itemInHand) {
        return getContainer(itemInHand).map(container -> {
            return findEmptySlot(itemInHand, container, false);
        }).orElse(-1);
    }

    public static Optional<SimpleContainer> getContainer(ItemStack itemInHand) {
        return itemInHand.hasTag() ? Optional.of(ContainerItemHelper.loadItemContainer(itemInHand, null, false)) : Optional.empty();
    }

    public static int findEmptySlot(ItemStack itemInHand, SimpleContainer container, boolean forward) {
        int selected = itemInHand.getTag().getInt(AlchemyBagItem.TAG_SELECTED);
        for (int i = 1; i < AlchemyBagMenu.ALCHEMY_BAG_SLOTS; i++) {
            int slot = (selected + i * (forward ? 1 : -1) + AlchemyBagMenu.ALCHEMY_BAG_SLOTS) % AlchemyBagMenu.ALCHEMY_BAG_SLOTS;
            if (!container.getItem(slot).isEmpty()) {
                return slot;
            }
        }
        return -1;
    }
}
