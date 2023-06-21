package fuzs.essentialpotions.network;

import fuzs.essentialpotions.client.handler.SlotRendererHandler;
import fuzs.essentialpotions.world.item.AlchemyBagItem;
import fuzs.puzzleslib.api.networking.v3.ServerMessageListener;
import fuzs.puzzleslib.api.networking.v3.ServerboundMessage;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ServerboundCyclePotionMessage(int selectedItem, InteractionHand interactionHand, boolean forward) implements ServerboundMessage<ServerboundCyclePotionMessage> {

    @Override
    public ServerMessageListener<ServerboundCyclePotionMessage> getHandler() {
        return new ServerMessageListener<>() {

            @Override
            public void handle(ServerboundCyclePotionMessage message, MinecraftServer server, ServerGamePacketListenerImpl handler, ServerPlayer player, ServerLevel level) {
                ItemStack itemInHand = player.getItemInHand(ServerboundCyclePotionMessage.this.interactionHand());
                if (SlotRendererHandler.INSTANCE.supportsSelectedItem(itemInHand)) {
                    setSelectedItem(itemInHand, ServerboundCyclePotionMessage.this.interactionHand(), player.getInventory(), ServerboundCyclePotionMessage.this.forward());
                }
            }
        };
    }

    public static void setSelectedItem(ItemStack itemInHand, InteractionHand interactionHand, Inventory inventory, boolean forward) {
        int slot;
        if (forward) {
            slot = SlotRendererHandler.INSTANCE.getForwardSlot(itemInHand, interactionHand, inventory);
        } else {
            slot = SlotRendererHandler.INSTANCE.getBackwardSlot(itemInHand, interactionHand, inventory);
        }
        itemInHand.getTag().putInt(AlchemyBagItem.TAG_SELECTED, slot);
        inventory.player.stopUsingItem();
    }
}
