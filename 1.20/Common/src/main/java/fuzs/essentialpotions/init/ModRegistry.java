package fuzs.essentialpotions.init;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.capability.EssentialPotionsCapability;
import fuzs.essentialpotions.capability.EssentialPotionsCapabilityImpl;
import fuzs.essentialpotions.world.inventory.AlchemyBagMenu;
import fuzs.puzzleslib.api.capability.v2.CapabilityController;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityKey;
import fuzs.puzzleslib.api.init.v2.RegistryManager;
import fuzs.puzzleslib.api.init.v2.RegistryReference;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;

public class ModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.instant(EssentialPotions.MOD_ID);
    public static final RegistryReference<Item> ALCHEMY_BAG_ITEM = REGISTRY.placeholder(Registries.ITEM, "alchemy_bag");
    public static final RegistryReference<MobEffect> PERPLEXITY_MOB_EFFECT = REGISTRY.registerMobEffect("perplexity", () -> new MobEffect(MobEffectCategory.HARMFUL, 10027263) {});
    public static final RegistryReference<Potion> PERPLEXITY_POTION = REGISTRY.registerPotion("perplexity", () -> new Potion(new MobEffectInstance(PERPLEXITY_MOB_EFFECT.get(), 3600)));
    public static final RegistryReference<Potion> LONG_PERPLEXITY_POTION = REGISTRY.registerPotion("long_perplexity", () -> new Potion("perplexity", new MobEffectInstance(PERPLEXITY_MOB_EFFECT.get(), 7200)));
    public static final RegistryReference<MenuType<AlchemyBagMenu>> ALCHEMY_BAG_MENU_TYPE = REGISTRY.registerMenuType("alchemy_bag", () -> AlchemyBagMenu::new);

    public static final TagKey<Item> DRINKABLE_POTIONS_ITEM_TAG = REGISTRY.registerItemTag("drinkable_potions");

    static final CapabilityController CAPABILITIES = CapabilityController.from(EssentialPotions.MOD_ID);
    public static final CapabilityKey<EssentialPotionsCapability> ESSENTIAL_POTIONS_CAPABILITY = CAPABILITIES.registerEntityCapability("main", EssentialPotionsCapability.class, EssentialPotionsCapabilityImpl::new, LivingEntity.class);

    public static void touch() {

    }
}
