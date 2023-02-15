package fuzs.essentialpotions.client;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = EssentialPotions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EssentialPotionsForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientFactories.INSTANCE.clientModConstructor(EssentialPotions.MOD_ID).accept(new EssentialPotionsClient());
    }

//    @SubscribeEvent
//    public static void onAddPackFinders(final AddPackFindersEvent evt) {
//        evt.addRepositorySource(new RepositorySource() {
//            @Override
//            public void loadPacks(Consumer<Pack> pInfoConsumer, Pack.PackConstructor pInfoFactory) {
//                Pack pack = Pack.create("vanilla", true, () -> {
//                    return this.vanillaPack;
//                }, pInfoFactory, Pack.Position.BOTTOM, PackSource.BUILT_IN);
//                if (pack != null) {
//                    pInfoConsumer.accept(pack);
//                }
//            }
//        });
//    }
}
