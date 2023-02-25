package fuzs.essentialpotions.world.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public class ContainerItemHelper {
    public static final String TAG_ITEMS = "Items";

    public static SimpleContainer loadItemContainer(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType) {
        return loadItemContainer(stack, blockEntityType, true);
    }

    public static SimpleContainer loadItemContainer(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, boolean allowSaving) {
        CompoundTag compoundtag = getDataTagFromItem(stack, blockEntityType);
        SimpleContainer container = new UnlimitedContainerWithSlots(AlchemyBagMenu.ALCHEMY_BAG_SLOTS);
        if (compoundtag != null && compoundtag.contains(TAG_ITEMS)) {
            container.fromTag(compoundtag.getList(TAG_ITEMS, Tag.TAG_COMPOUND));
        }
        if (allowSaving) {
            container.addListener(container1 -> {
                saveItemContainer(stack, blockEntityType, (SimpleContainer) container1);
            });
        }
        return container;
    }

    private static void saveItemContainer(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType, SimpleContainer container) {
        ListTag listTag = container.createTag();
        if (blockEntityType == null) {
            if (listTag.isEmpty()) {
                stack.removeTagKey(TAG_ITEMS);
            } else {
                stack.addTagElement(TAG_ITEMS, listTag);
            }
        } else {
            CompoundTag tag = BlockItem.getBlockEntityData(stack);
            if (tag == null) {
                tag = new CompoundTag();
            } else {
                tag.remove(TAG_ITEMS);
            }
            if (!listTag.isEmpty()) {
                tag.put(TAG_ITEMS, listTag);
            }
            BlockItem.setBlockEntityData(stack, blockEntityType, tag);
        }
    }

    @Nullable
    private static CompoundTag getDataTagFromItem(ItemStack stack, @Nullable BlockEntityType<?> blockEntityType) {
        return blockEntityType != null ? BlockItem.getBlockEntityData(stack) : stack.getTag();
    }
}
