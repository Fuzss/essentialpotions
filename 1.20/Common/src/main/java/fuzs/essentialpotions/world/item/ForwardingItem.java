package fuzs.essentialpotions.world.item;

import net.minecraft.world.item.ItemStack;

public interface ForwardingItem {
    String TAG_SELECTED = "Selected";

    ItemStack getSelectedItem(ItemStack stack);

    void setSelectedItem(ItemStack stack, ItemStack selectedItem);
}
