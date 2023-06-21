package fuzs.essentialpotions.client.handler;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.client.init.ClientModRegistry;
import fuzs.essentialpotions.config.ClientConfig;
import fuzs.essentialpotions.mixin.client.accessor.ItemInHandRendererAccessor;
import fuzs.essentialpotions.world.item.AlchemyBagItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

public class KeyBindingHandler {
    public static int cycleSlotsDisplay;
    public static int globalPopTime;
    public static int toolHighlightTimer;
    public static ItemStack lastToolHighlight = ItemStack.EMPTY;

    public static void onClientTick$Start(Minecraft minecraft) {
        if (minecraft.player == null) return;
        if (cycleSlotsDisplay > 0) cycleSlotsDisplay--;
        if (globalPopTime > 0) globalPopTime--;
        if (toolHighlightTimer > 0) {
            toolHighlightTimer--;
            boolean clearLastToolHighlight = true;
            for (InteractionHand interactionHand : InteractionHand.values()) {
                ItemStack itemInHand = minecraft.player.getItemInHand(interactionHand);
                if (SlotRendererHandler.INSTANCE.supportsSelectedItem(itemInHand)) {
                    clearLastToolHighlight = false;
                }
            }
            if (clearLastToolHighlight) lastToolHighlight = ItemStack.EMPTY;
        }
        if (minecraft.getOverlay() == null && (minecraft.screen == null || minecraft.screen.passEvents)) {
            handleKeybinds(minecraft, minecraft.player);
        }
    }

    private static void setLastToolHighlight(Minecraft minecraft, ItemStack itemInHand, InteractionHand interactionHand) {
        globalPopTime = 5;
        toolHighlightTimer = 40;
        lastToolHighlight = ((AlchemyBagItem) itemInHand.getItem()).getSelectedItem(itemInHand);
        clearItemInHand(minecraft, interactionHand);
    }

    private static void clearItemInHand(Minecraft minecraft, InteractionHand interactionHand) {
        ItemInHandRenderer itemInHandRenderer = minecraft.gameRenderer.itemInHandRenderer;
        if (interactionHand == InteractionHand.OFF_HAND) {
            ((ItemInHandRendererAccessor) itemInHandRenderer).essentialpotions$setOffHandItem(ItemStack.EMPTY);
        } else {
            ((ItemInHandRendererAccessor) itemInHandRenderer).essentialpotions$setMainHandItem(ItemStack.EMPTY);
        }
    }

    private static void handleKeybinds(Minecraft minecraft, Player player) {
        if (!player.isSpectator()) {
            while (ClientModRegistry.CYCLE_LEFT_KEY_MAPPING.consumeClick()) {
                for (InteractionHand interactionHand : InteractionHand.values()) {
                    ItemStack itemInHand = player.getItemInHand(interactionHand);
                    if (SlotRendererHandler.INSTANCE.supportsSelectedItem(itemInHand)) {
                        if (SlotRendererHandler.INSTANCE.cycleSlotBackward(itemInHand, interactionHand, player.getInventory())) {
                            setLastToolHighlight(minecraft, itemInHand, interactionHand);
                            break;
                        }
                    }
                }
            }
            while (ClientModRegistry.CYCLE_RIGHT_KEY_MAPPING.consumeClick()) {
                for (InteractionHand interactionHand : InteractionHand.values()) {
                    ItemStack itemInHand = player.getItemInHand(interactionHand);
                    if (SlotRendererHandler.INSTANCE.supportsSelectedItem(itemInHand)) {
                        if (SlotRendererHandler.INSTANCE.cycleSlotForward(itemInHand, interactionHand, player.getInventory())) {
                            setLastToolHighlight(minecraft, itemInHand, interactionHand);
                            break;
                        }
                    }
                }
            }
        }
        if (EssentialPotions.CONFIG.get(ClientConfig.class).scrollingModifierKey.active()) {
            cycleSlotsDisplay = 15;
        }
    }

    public static void setPopTimeColumn(Player player) {
        final int selected = player.getInventory().selected;
        for (int i = 0; i < 4; i++) {
            final ItemStack itemStack = player.getInventory().items.get(selected + 9 * i);
            if (!itemStack.isEmpty()) itemStack.setPopTime(5);
        }
    }

    public static void swapSlots(Player player, int currentSlot, int nextSlot) {
        final Minecraft minecraft = Minecraft.getInstance();
        minecraft.gameMode.handleInventoryMouseClick(player.containerMenu.containerId, currentSlot, nextSlot, ClickType.SWAP, player);
    }
}
