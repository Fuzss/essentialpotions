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

public record ServerboundCyclePotionMessage(int selectedItem, InteractionHand interactionHand, boolean forward) implements ServerboundMessage<ServerboundCyclePotionMessage> {

    @Override
    public ServerMessageListener<ServerboundCyclePotionMessage> getHandler() {
        return new ServerMessageListener<>() {

            @Override
            public void handle(ServerboundCyclePotionMessage message, MinecraftServer server, ServerGamePacketListenerImpl handler, ServerPlayer player, ServerLevel level) {
                setSelectedItem(player, ServerboundCyclePotionMessage.this.interactionHand(), ServerboundCyclePotionMessage.this.forward());
            }
        };
    }

    public static void setSelectedItem(Player player, InteractionHand interactionHand, boolean forward) {
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        if (SlotRendererHandler.INSTANCE.supportsSelectedItem(itemInHand)) {
            int slot;
            if (forward) {
                slot = SlotRendererHandler.INSTANCE.getForwardSlot(player.getInventory());
            } else {
                slot = SlotRendererHandler.INSTANCE.getBackwardSlot(player.getInventory());
            }
            itemInHand.getTag().putInt(AlchemyBagItem.TAG_SELECTED, slot);
            player.stopUsingItem();
        }
    }
}
