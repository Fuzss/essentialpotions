package fuzs.essentialpotions.init;

import fuzs.essentialpotions.world.item.AlchemyBagFabricItem;
import fuzs.puzzleslib.init.RegistryReference;
import net.minecraft.world.item.Item;

import static fuzs.essentialpotions.init.ModRegistry.CREATIVE_MODE_TAB;
import static fuzs.essentialpotions.init.ModRegistry.REGISTRY;

public class FabricModRegistry {
    public static final RegistryReference<Item> ALCHEMY_BAG_ITEM = REGISTRY.registerItem("alchemy_bag", () -> new AlchemyBagFabricItem(new Item.Properties().tab(CREATIVE_MODE_TAB).stacksTo(1)));

    public static void touch() {

    }
}
