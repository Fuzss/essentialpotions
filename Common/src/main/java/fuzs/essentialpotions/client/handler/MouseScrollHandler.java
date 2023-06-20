package fuzs.essentialpotions.client.handler;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class MouseScrollHandler {

    public static Optional<Unit> onMouseScroll(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (!player.isSpectator() && EssentialPotions.CONFIG.get(ClientConfig.class).scrollingModifierKey.active()) {
//            double totalScroll = verticalAmount + ((MouseHandlerAccessor) minecraft.mouseHandler).getAccumulatedScroll();
            double totalScroll = verticalAmount;
            if (totalScroll > 0.0) {
                if (SlotUtil.cycleSlotsLeft(player, KeyBindingHandler::swapSlots)) {
                    KeyBindingHandler.setPopTimeColumn(player);
                }
            } else if (totalScroll < 0.0) {
                if (SlotUtil.cycleSlotsRight(player, KeyBindingHandler::swapSlots)) {
                    KeyBindingHandler.setPopTimeColumn(player);
                }
            }
            return Optional.of(Unit.INSTANCE);
        }
        return Optional.empty();
    }
}
