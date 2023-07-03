package fuzs.essentialpotions.world.inventory;

import fuzs.puzzlesapi.api.limitlesscontainers.v1.LimitlessContainerUtils;
import fuzs.puzzlesapi.api.limitlesscontainers.v1.MultipliedSimpleContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.IntFunction;

public class LimitlessSimpleSlotContainer extends MultipliedSimpleContainer {

    public LimitlessSimpleSlotContainer(int stackSizeMultiplier, int size) {
        super(stackSizeMultiplier, size);
    }

    public LimitlessSimpleSlotContainer(int stackSizeMultiplier, ItemStack... items) {
        super(stackSizeMultiplier, items);
    }

    @Override
    public void fromTag(ListTag containerNbt) {
        loadAllItems(containerNbt, this::setItem, this.getContainerSize());
    }

    @Override
    public ListTag createTag() {
        return saveAllItems(this::getItem, this.getContainerSize());
    }

    public static ListTag saveAllItems(IntFunction<ItemStack> extractor, int containerSize) {
        ListTag list = new ListTag();

        for (int i = 0; i < containerSize; ++i) {
            ItemStack itemStack = extractor.apply(i);
            if (!itemStack.isEmpty()) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putByte("Slot", (byte) i);
                itemStack.save(compoundTag);
                compoundTag.putInt("Count", itemStack.getCount());
                list.add(compoundTag);
            }
        }

        return list;
    }

    public static void loadAllItems(CompoundTag tag, NonNullList<ItemStack> items) {
        loadAllItems(tag.getList("Items", Tag.TAG_COMPOUND), items::set, items.size());
    }

    public static void loadAllItems(ListTag list, BiConsumer<Integer, ItemStack> consumer, int containerSize) {
        for (int i = 0; i < list.size(); ++i) {
            CompoundTag compoundTag = list.getCompound(i);
            int j = compoundTag.getByte("Slot") & 255;
            if (j < containerSize) {
                ItemStack itemStack = ItemStack.of(compoundTag);
                itemStack.setCount(compoundTag.getInt("Count"));
                consumer.accept(j, itemStack);
            }
        }
    }
}
