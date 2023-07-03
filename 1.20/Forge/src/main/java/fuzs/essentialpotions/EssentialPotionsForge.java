package fuzs.essentialpotions;

import fuzs.essentialpotions.data.*;
import fuzs.essentialpotions.init.ForgeModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EssentialPotions.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EssentialPotionsForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ForgeModRegistry.touch();
        ModConstructor.construct(EssentialPotions.MOD_ID, EssentialPotions::new);
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator dataGenerator = evt.getGenerator();
        dataGenerator.addProvider(true, new ModModelProvider(evt, EssentialPotions.MOD_ID));
        dataGenerator.addProvider(true, new ModItemContainerProvider(evt, EssentialPotions.MOD_ID));
        dataGenerator.addProvider(true, new ModItemTagsProvider(evt, EssentialPotions.MOD_ID));
        dataGenerator.addProvider(true, new ModLanguageProvider(evt, EssentialPotions.MOD_ID));
        dataGenerator.addProvider(true, new ModRecipeProvider(evt, EssentialPotions.MOD_ID));
    }
}
