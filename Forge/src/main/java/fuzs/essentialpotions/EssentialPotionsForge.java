package fuzs.essentialpotions;

import fuzs.essentialpotions.data.ModBlockStateProvider;
import fuzs.essentialpotions.data.ModItemTagsProvider;
import fuzs.essentialpotions.data.ModLanguageProvider;
import fuzs.essentialpotions.data.ModRecipeProvider;
import fuzs.puzzleslib.core.CommonFactories;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EssentialPotions.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EssentialPotionsForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CommonFactories.INSTANCE.modConstructor(EssentialPotions.MOD_ID).accept(new EssentialPotions());
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator dataGenerator = evt.getGenerator();
        final ExistingFileHelper fileHelper = evt.getExistingFileHelper();
        dataGenerator.addProvider(true, new ModBlockStateProvider(dataGenerator, EssentialPotions.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModItemTagsProvider(dataGenerator, EssentialPotions.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModLanguageProvider(dataGenerator, EssentialPotions.MOD_ID));
        dataGenerator.addProvider(true, new ModRecipeProvider(dataGenerator));
    }
}
