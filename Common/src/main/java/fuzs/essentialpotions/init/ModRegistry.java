package fuzs.essentialpotions.init;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.world.item.AlchemyBagItem;
import fuzs.puzzleslib.core.CommonAbstractions;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModRegistry {
    public static final CreativeModeTab CREATIVE_MODE_TAB = CommonAbstractions.INSTANCE.creativeModeTabBuilder(EssentialPotions.MOD_ID).setIcon(() -> new ItemStack(Items.POTION)).appendAllPotions().build();
    private static final RegistryManager REGISTRY = CommonFactories.INSTANCE.registration(EssentialPotions.MOD_ID);
    public static final RegistryReference<Item> ALCHEMY_BAG_ITEM = REGISTRY.registerItem("alchemy_bag", () -> new AlchemyBagItem(new Item.Properties().tab(CREATIVE_MODE_TAB).stacksTo(1)));

    public static final TagKey<Item> DRINKABLE_POTIONS_ITEM_TAG = TagKey.create(Registry.ITEM_REGISTRY, EssentialPotions.id("drinkable_potions"));

    public static void touch() {

    }
}
