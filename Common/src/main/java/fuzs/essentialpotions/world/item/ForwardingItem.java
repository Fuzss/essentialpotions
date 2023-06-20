package fuzs.essentialpotions.world.item;

import net.minecraft.world.item.ItemStack;

public interface ForwardingItem {

    ItemStack getSelectedItem(ItemStack stack);

    boolean isFoilSelf(ItemStack stack);
}
