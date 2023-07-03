package fuzs.essentialpotions.client.handler;

import fuzs.puzzlesapi.api.client.slotcycling.v1.SlotCyclingProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class ForwardingItemCyclingHandler {
    private static int toolHighlightTimer;
    private static ItemStack lastToolHighlight = ItemStack.EMPTY;

    public static void onClientTick$Start(Minecraft minecraft) {
        if (minecraft.player != null && !minecraft.player.isSpectator()) {
            if (toolHighlightTimer > 0) {
                toolHighlightTimer--;
                if (!(SlotCyclingProvider.getProvider(minecraft.player) instanceof ForwardingCyclingProvider)) {
                    lastToolHighlight = ItemStack.EMPTY;
                }
            }
        }
    }

    public static int getToolHighlightTimer() {
        return toolHighlightTimer;
    }

    public static ItemStack getLastToolHighlight() {
        return lastToolHighlight;
    }

    public static void setLastToolHighlight(ItemStack lastToolHighlight) {
        if (ForwardingItemCyclingHandler.lastToolHighlight.isEmpty() || !lastToolHighlight.is(ForwardingItemCyclingHandler.lastToolHighlight.getItem()) || !lastToolHighlight.getHoverName().equals(ForwardingItemCyclingHandler.lastToolHighlight.getHoverName())) {
            toolHighlightTimer = (int) (40 * Minecraft.getInstance().options.notificationDisplayTime().get());
            ForwardingItemCyclingHandler.lastToolHighlight = lastToolHighlight;
        }
    }
}
