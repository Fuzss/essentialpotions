package fuzs.essentialpotions.networking;

import fuzs.puzzleslib.api.networking.v3.ServerMessageListener;
import fuzs.puzzleslib.api.networking.v3.ServerboundMessage;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public record ServerboundCyclePotionMessage(int selectedItem, boolean left) implements ServerboundMessage<ServerboundCyclePotionMessage> {

    @Override
    public ServerMessageListener<ServerboundCyclePotionMessage> getHandler() {
        return new ServerMessageListener<>() {

            @Override
            public void handle(ServerboundCyclePotionMessage message, MinecraftServer server, ServerGamePacketListenerImpl handler, ServerPlayer player, ServerLevel level) {
                handler.handleSetCarriedItem(new ServerboundSetCarriedItemPacket(message.selectedItem()));

            }
        };
    }
}
