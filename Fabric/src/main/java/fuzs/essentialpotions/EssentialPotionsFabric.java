package fuzs.essentialpotions;

import fuzs.puzzleslib.core.CommonFactories;
import net.fabricmc.api.ModInitializer;

public class EssentialPotionsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonFactories.INSTANCE.modConstructor(EssentialPotions.MOD_ID).accept(new EssentialPotions());
    }
}
