package fuzs.essentialpotions.world.inventory;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class UnlimitedContainerWithSlots extends UnlimitedContainer {

    public UnlimitedContainerWithSlots(int size) {
        super(size);
    }

    public UnlimitedContainerWithSlots(ItemStack... itemStacks) {
        super(itemStacks);
    }

    @Override
    public void fromTag(ListTag containerNbt) {
        UnlimitedContainerUtils.loadAllItems(containerNbt, this::setItem, this.getContainerSize());
    }

    @Override
    public ListTag createTag() {
        return UnlimitedContainerUtils.saveAllItems(this::getItem, this.getContainerSize());
    }
}
