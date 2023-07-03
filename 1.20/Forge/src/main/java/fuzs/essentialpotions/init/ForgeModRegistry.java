package fuzs.essentialpotions.init;

import fuzs.essentialpotions.world.item.AlchemyBagForgeItem;
import fuzs.puzzleslib.api.init.v2.RegistryReference;
import net.minecraft.world.item.Item;

import static fuzs.essentialpotions.init.ModRegistry.REGISTRY;

public class ForgeModRegistry {
    public static final RegistryReference<Item> ALCHEMY_BAG_ITEM = REGISTRY.registerItem("alchemy_bag", () -> new AlchemyBagForgeItem(new Item.Properties().stacksTo(1)));

    public static void touch() {

    }
}
