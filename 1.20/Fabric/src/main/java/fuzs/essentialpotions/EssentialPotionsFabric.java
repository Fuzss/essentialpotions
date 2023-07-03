package fuzs.essentialpotions;

import fuzs.essentialpotions.init.FabricModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class EssentialPotionsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricModRegistry.touch();
        ModConstructor.construct(EssentialPotions.MOD_ID, EssentialPotions::new);
    }
}
