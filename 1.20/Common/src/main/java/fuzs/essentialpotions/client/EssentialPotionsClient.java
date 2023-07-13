package fuzs.essentialpotions.client;

import fuzs.essentialpotions.client.gui.screens.AlchemyBagScreen;
import fuzs.essentialpotions.client.handler.ForwardingCyclingProvider;
import fuzs.essentialpotions.client.handler.ForwardingItemCyclingHandler;
import fuzs.essentialpotions.client.handler.VanillaEffectsClientHandler;
import fuzs.essentialpotions.client.renderer.AlchemyBagItemRenderer;
import fuzs.essentialpotions.init.ModRegistry;
import fuzs.puzzlesapi.api.client.slotcycling.v1.SlotCyclingProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.AdditionalModelsContext;
import fuzs.puzzleslib.api.client.core.v1.context.BuiltinModelItemRendererContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.FogEvents;
import fuzs.puzzleslib.api.client.event.v1.RenderBlockOverlayCallback;
import net.minecraft.client.gui.screens.MenuScreens;

public class EssentialPotionsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ClientTickEvents.START.register(ForwardingItemCyclingHandler::onClientTick$Start);
        FogEvents.RENDER.register(VanillaEffectsClientHandler::onRenderFog$1);
        FogEvents.RENDER.register(VanillaEffectsClientHandler::onRenderFog$2);
        RenderBlockOverlayCallback.EVENT.register(VanillaEffectsClientHandler::onRenderBlockOverlay);
    }

    @Override
    public void onClientSetup() {
        MenuScreens.register(ModRegistry.ALCHEMY_BAG_MENU_TYPE.get(), AlchemyBagScreen::new);
        SlotCyclingProvider.registerProvider(ModRegistry.ALCHEMY_BAG_ITEM.get(), ForwardingCyclingProvider::new);
    }

    @Override
    public void onRegisterAdditionalModels(AdditionalModelsContext context) {
        context.registerAdditionalModel(AlchemyBagItemRenderer.ALCHEMY_BAG_ITEM_MODEL);
    }

    @Override
    public void onRegisterBuiltinModelItemRenderers(BuiltinModelItemRendererContext context) {
        context.registerItemRenderer(new AlchemyBagItemRenderer(), ModRegistry.ALCHEMY_BAG_ITEM.get());
    }
}
