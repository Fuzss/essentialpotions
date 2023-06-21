package fuzs.essentialpotions;

import fuzs.essentialpotions.init.FabricModRegistry;
import fuzs.puzzleslib.core.CommonFactories;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItem;

public class EssentialPotionsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricModRegistry.touch();
        CommonFactories.INSTANCE.modConstructor(EssentialPotions.MOD_ID).accept(new EssentialPotions());
    }
}
