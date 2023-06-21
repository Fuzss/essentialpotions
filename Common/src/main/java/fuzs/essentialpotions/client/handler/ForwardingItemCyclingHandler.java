package fuzs.essentialpotions.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class ForwardingItemCyclingHandler {
    public static int toolHighlightTimer;
    public static ItemStack lastToolHighlight = ItemStack.EMPTY;

    public static void onClientTick$Start(Minecraft minecraft) {
        if (minecraft.player != null && !minecraft.player.isSpectator()) {
            if (toolHighlightTimer > 0) {
                toolHighlightTimer--;
                if (SlotCyclingProvider.getProvider(minecraft.player) == null) {
                    lastToolHighlight = ItemStack.EMPTY;
                }
            }
        }
    }
}
