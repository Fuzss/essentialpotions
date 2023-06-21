package fuzs.essentialpotions.client.handler;

import com.google.common.collect.Maps;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface SlotCyclingProvider {
    List<SlotCyclingProvider> GLOBAL_PROVIDERS = Lists.newArrayList();
    Map<Item, ItemCyclingProvider.Factory> ITEM_PROVIDERS = Maps.newIdentityHashMap();

    static void registerProvider(SlotCyclingProvider provider) {
        GLOBAL_PROVIDERS.add(provider);
    }

    static void registerProvider(Item item, ItemCyclingProvider.Factory factory) {
        if (ITEM_PROVIDERS.put(item, factory) != null) {
            throw new IllegalStateException("Duplicate item cycling provider for item " + item);
        }
    }

    @Nullable
    static SlotCyclingProvider getProvider(Player player) {
        for (Map.Entry<Item, ItemCyclingProvider.Factory> entry : ITEM_PROVIDERS.entrySet()) {
            for (InteractionHand interactionHand : InteractionHand.values()) {
                ItemStack itemInHand = player.getItemInHand(interactionHand);
                if (itemInHand.is(entry.getKey())) {
                    return entry.getValue().apply(itemInHand, interactionHand);
                }
            }
        }
        return !GLOBAL_PROVIDERS.isEmpty() ? GLOBAL_PROVIDERS.get(0) : null;
    }

    @Nullable
    static SlotCyclingProvider getProvider(Player player, InteractionHand interactionHand) {
        for (Map.Entry<Item, ItemCyclingProvider.Factory> entry : ITEM_PROVIDERS.entrySet()) {
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            if (itemInHand.is(entry.getKey())) {
                return entry.getValue().apply(itemInHand, interactionHand);
            }
        }
        return !GLOBAL_PROVIDERS.isEmpty() ? GLOBAL_PROVIDERS.get(0) : null;
    }

    ItemStack getSelectedStack();

    ItemStack getForwardStack();

    ItemStack getBackwardStack();

    int getForwardSlot();

    int getBackwardSlot();

    boolean cycleSlotForward();

    boolean cycleSlotBackward();
}
