package fuzs.essentialpotions.client.init;

import fuzs.essentialpotions.EssentialPotions;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class ClientModRegistry {
    public static final ModelResourceLocation ALCHEMY_BAG_ITEM_MODEL = new ModelResourceLocation(EssentialPotions.id("alchemy_bag_model"), "inventory");
    public static final ModelResourceLocation ALCHEMY_BAG_IN_HAND_ITEM_MODEL = new ModelResourceLocation(EssentialPotions.id("alchemy_bag_in_hand"), "inventory");
}
