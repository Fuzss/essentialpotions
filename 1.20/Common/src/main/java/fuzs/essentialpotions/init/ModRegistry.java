package fuzs.essentialpotions.init;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.world.inventory.AlchemyBagMenu;
import fuzs.puzzleslib.api.init.v2.RegistryManager;
import fuzs.puzzleslib.api.init.v2.RegistryReference;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;

public class ModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.instant(EssentialPotions.MOD_ID);
    public static final RegistryReference<Item> ALCHEMY_BAG_ITEM = REGISTRY.placeholder(Registries.ITEM, "alchemy_bag");
    public static final RegistryReference<MenuType<AlchemyBagMenu>> ALCHEMY_BAG_MENU_TYPE = REGISTRY.registerMenuType("alchemy_bag", () -> AlchemyBagMenu::new);

    public static final TagKey<Item> DRINKABLE_POTIONS_ITEM_TAG = REGISTRY.registerItemTag("drinkable_potions");

    public static void touch() {

    }
}
