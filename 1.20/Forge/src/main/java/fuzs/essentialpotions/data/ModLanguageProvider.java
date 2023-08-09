package fuzs.essentialpotions.data;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void addTranslations() {
        this.addCreativeModeTab(EssentialPotions.MOD_NAME);
        this.add(ModRegistry.ALCHEMY_BAG_ITEM.get(), "Alchemy Bag");
        this.add("container.alchemy_bag", "Alchemy Bag");
        this.add(ModRegistry.PERPLEXITY_MOB_EFFECT.get(), "Perplexity");
        this.add(ModRegistry.PERPLEXITY_POTION.get(), "Perplexity");
    }
}
