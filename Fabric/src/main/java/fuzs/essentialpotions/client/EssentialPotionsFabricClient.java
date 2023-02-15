package fuzs.essentialpotions.client;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.fabricmc.api.ClientModInitializer;

public class EssentialPotionsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientFactories.INSTANCE.clientModConstructor(EssentialPotions.MOD_ID).accept(new EssentialPotionsClient());
    }
}
