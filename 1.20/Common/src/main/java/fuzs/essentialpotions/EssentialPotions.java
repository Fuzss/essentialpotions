package fuzs.essentialpotions;

import fuzs.essentialpotions.config.ServerConfig;
import fuzs.essentialpotions.handler.VanillaEffectsHandler;
import fuzs.essentialpotions.init.ModRegistry;
import fuzs.essentialpotions.network.ServerboundCyclePotionMessage;
import fuzs.essentialpotions.world.item.AlchemyBagProvider;
import fuzs.puzzlesapi.api.iteminteractions.v1.ItemContainerProviderSerializers;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.CreativeModeTabContext;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingEvents;
import fuzs.puzzleslib.api.item.v2.CreativeModeTabConfigurator;
import fuzs.puzzleslib.api.network.v3.NetworkHandlerV3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EssentialPotions implements ModConstructor {
    public static final String MOD_ID = "essentialpotions";
    public static final String MOD_NAME = "Essential Potions";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).server(ServerConfig.class);
    public static final NetworkHandlerV3 NETWORK = NetworkHandlerV3.builder(MOD_ID).registerServerbound(ServerboundCyclePotionMessage.class);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
        ItemContainerProviderSerializers.register(AlchemyBagProvider.class, id("bag"), jsonElement -> {
            return new AlchemyBagProvider();
        });
        registerHandlers();
    }

    private static void registerHandlers() {
        LivingEvents.TICK.register(VanillaEffectsHandler::onLivingTick);
        LivingEvents.VISIBILITY.register(VanillaEffectsHandler::onLivingVisibility);
    }

    @Override
    public void onRegisterCreativeModeTabs(CreativeModeTabContext context) {
        context.registerCreativeModeTab(CreativeModeTabConfigurator.from(MOD_ID).icon(() -> new ItemStack(Items.POTION)).displayItems((itemDisplayParameters, output) -> {
            output.accept(ModRegistry.ALCHEMY_BAG_ITEM.get());
        }).appendEnchantmentsAndPotions());
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
