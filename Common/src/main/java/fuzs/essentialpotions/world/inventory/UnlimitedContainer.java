package fuzs.essentialpotions.world.inventory;

import fuzs.essentialpotions.world.item.AlchemyBagItem;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class UnlimitedContainer extends SimpleContainer {

    public UnlimitedContainer(int size) {
        super(size);
    }

    public UnlimitedContainer(ItemStack... itemStacks) {
        super(itemStacks);
    }

    @Override
    public int getMaxStackSize() {
        return AlchemyBagItem.POTION_MAX_STACK_SIZE;
    }
}
