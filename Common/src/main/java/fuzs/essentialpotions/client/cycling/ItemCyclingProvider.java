package fuzs.essentialpotions.client.cycling;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public interface ItemCyclingProvider extends SlotCyclingProvider {

    ItemStack itemInHand();

    InteractionHand interactionHand();

    interface Factory {

        ItemCyclingProvider apply(ItemStack itemInHand, InteractionHand interactionHand);
    }
}
