package fuzs.essentialpotions.client;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.core.v1.ContentRegistrationFlags;
import net.fabricmc.api.ClientModInitializer;

public class EssentialPotionsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(EssentialPotions.MOD_ID, EssentialPotionsClient::new, ContentRegistrationFlags.DYNAMIC_RENDERERS);
    }
}
