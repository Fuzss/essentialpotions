package fuzs.essentialpotions.world.item;

import net.minecraft.world.item.ItemStack;

public interface ForwardingItem {

    ItemStack getSelectedItem(ItemStack stack);

    boolean setSelectedItem(ItemStack stack, ItemStack selectedItem);

    boolean isFoilSelf(ItemStack stack);

    default boolean isAllowedInside(ItemStack stack) {
        return true;
    }
}
