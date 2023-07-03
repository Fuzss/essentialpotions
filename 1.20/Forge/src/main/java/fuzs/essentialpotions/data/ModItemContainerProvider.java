package fuzs.essentialpotions.data;

import fuzs.essentialpotions.init.ModRegistry;
import fuzs.essentialpotions.world.item.AlchemyBagProvider;
import fuzs.puzzlesapi.api.iteminteractions.v1.data.AbstractItemContainerProvider;
import net.minecraftforge.data.event.GatherDataEvent;

public class ModItemContainerProvider extends AbstractItemContainerProvider {

    public ModItemContainerProvider(GatherDataEvent evt, String modId) {
        super(evt.getGenerator().getPackOutput());
    }

    @Override
    protected void registerBuiltInProviders() {
        this.add(ModRegistry.ALCHEMY_BAG_ITEM.get(), new AlchemyBagProvider());
    }
}
