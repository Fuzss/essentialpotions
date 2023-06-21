package fuzs.essentialpotions.client;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.client.handler.KeyBindingHandler;
import fuzs.essentialpotions.client.handler.SlotRendererHandler;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;

public class EssentialPotionsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientFactories.INSTANCE.clientModConstructor(EssentialPotions.MOD_ID).accept(new EssentialPotionsClient());
        registerHandlers();
    }

    private static void registerHandlers() {
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            Minecraft minecraft = Minecraft.getInstance();
            SlotRendererHandler.tryRenderSlots(minecraft, matrixStack, tickDelta, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
        });
        ClientTickEvents.START_CLIENT_TICK.register(KeyBindingHandler::onClientTick$Start);
    }
}
