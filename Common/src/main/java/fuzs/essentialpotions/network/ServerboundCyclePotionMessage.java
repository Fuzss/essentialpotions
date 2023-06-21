package fuzs.essentialpotions.network;

import fuzs.essentialpotions.helper.AlchemyBagHelper;
import fuzs.essentialpotions.world.item.AlchemyBagItem;
import fuzs.puzzleslib.api.networking.v3.ServerMessageListener;
import fuzs.puzzleslib.api.networking.v3.ServerboundMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record ServerboundCyclePotionMessage(InteractionHand interactionHand, int slot, boolean forward) implements ServerboundMessage<ServerboundCyclePotionMessage> {

    @Override
    public ServerMessageListener<ServerboundCyclePotionMessage> getHandler() {
        return new ServerMessageListener<>() {

            @Override
            public void handle(ServerboundCyclePotionMessage message, MinecraftServer server, ServerGamePacketListenerImpl handler, ServerPlayer player, ServerLevel level) {
                ItemStack itemInHand = player.getItemInHand(message.interactionHand());
                int slot = message.forward() ? AlchemyBagHelper.getForwardSlot(itemInHand) : AlchemyBagHelper.getBackwardSlot(itemInHand);
                if (slot == message.slot()) {
                    itemInHand.getTag().putInt(AlchemyBagItem.TAG_SELECTED, slot);
                    player.stopUsingItem();
                }
            }
        };
    }
}
