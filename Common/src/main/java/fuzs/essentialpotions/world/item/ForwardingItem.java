package fuzs.essentialpotions.world.item;

import net.minecraft.world.item.ItemStack;

public interface ForwardingItem {

    ItemStack getRenderItem(ItemStack stack);

    boolean isFoilSelf(ItemStack stack);
}
