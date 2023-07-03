package fuzs.essentialpotions.world.item;

import com.google.gson.JsonObject;
import fuzs.essentialpotions.init.ModRegistry;
import fuzs.essentialpotions.world.inventory.LimitlessSimpleSlotContainer;
import fuzs.puzzlesapi.api.iteminteractions.v1.ContainerItemHelper;
import fuzs.puzzlesapi.api.iteminteractions.v1.provider.SimpleItemProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class AlchemyBagProvider extends SimpleItemProvider {

    public AlchemyBagProvider() {
        super(9, 1, DyeColor.BROWN);
    }

    @Override
    public SimpleContainer getItemContainer(ItemStack containerStack, Player player, boolean allowSaving) {
        return ContainerItemHelper.INSTANCE.loadItemContainer(containerStack, this, items -> new LimitlessSimpleSlotContainer(AlchemyBagItem.POTION_STACK_SIZE_MULTIPLIER, this.getInventorySize()), allowSaving, this.getNbtKey());
    }

    @Override
    public boolean isItemAllowedInContainer(ItemStack containerStack, ItemStack stackToAdd) {
        return stackToAdd.is(ModRegistry.DRINKABLE_POTIONS_ITEM_TAG);
    }

    @Override
    public void toJson(JsonObject jsonObject) {

    }
}
