package fuzs.essentialpotions.world.inventory;

import fuzs.essentialpotions.init.ModRegistry;
import fuzs.essentialpotions.world.item.AlchemyBagItem;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AlchemyBagMenu extends UnlimitedContainerMenu {
    public static final int ALCHEMY_BAG_SLOTS = 9;
    public static final int HOTBAR_SLOTS_START = ALCHEMY_BAG_SLOTS + 27;

    private final Container container;

    public AlchemyBagMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new UnlimitedContainer(ALCHEMY_BAG_SLOTS));
    }

    public AlchemyBagMenu(int containerId, Inventory inventory, Container container) {
        super(ModRegistry.ALCHEMY_BAG_MENU_TYPE.get(), containerId);
        checkContainerSize(container, ALCHEMY_BAG_SLOTS);
        this.container = container;

        for (int m = 0; m < 9; ++m) {
            this.addSlot(new UnlimitedSlot(container, m, 8 + m * 18, 19) {

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(ModRegistry.DRINKABLE_POTIONS_ITEM_TAG);
                }
            });
        }

        for (int l = 0; l < 3; ++l) {
            for (int m = 0; m < 9; ++m) {
                this.addSlot(new Slot(inventory, m + l * 9 + 9, 8 + m * 18, 55 + l * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            boolean selectedSlot = l == inventory.selected;
            this.addSlot(new Slot(inventory, l, 8 + l * 18, 113) {

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return !selectedSlot || !(this.getItem().getItem() instanceof AlchemyBagItem);
                }

                @Override
                public boolean mayPickup(Player player) {
                    return !selectedSlot || !(this.getItem().getItem() instanceof AlchemyBagItem);
                }
            });
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index < 9) {
                if (!this.moveItemStackTo(itemStack2, 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 0, 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }
}