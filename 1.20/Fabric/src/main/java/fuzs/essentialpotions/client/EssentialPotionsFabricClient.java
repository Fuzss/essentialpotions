package fuzs.essentialpotions.client;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.ModelEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.resources.ResourceLocation;

public class EssentialPotionsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(EssentialPotions.MOD_ID, EssentialPotionsClient::new);
        ResourceLocation id = EssentialPotions.id("before");
        ModelEvents.MODIFY_BAKING_RESULT.addPhaseOrdering(id, Event.DEFAULT_PHASE);
        ModelEvents.MODIFY_BAKING_RESULT.register(id, (models, modelBakery) -> EssentialPotionsClient.onModifyBakingResult(models));
    }
}
