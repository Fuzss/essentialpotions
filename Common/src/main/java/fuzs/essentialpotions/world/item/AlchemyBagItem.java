package fuzs.essentialpotions.world.item;

import fuzs.essentialpotions.init.ModRegistry;
import fuzs.essentialpotions.mixin.accessor.LivingEntityAccessor;
import fuzs.essentialpotions.mixin.accessor.UseOnContextAccessor;
import fuzs.essentialpotions.world.inventory.AlchemyBagMenu;
import fuzs.essentialpotions.world.inventory.ContainerItemHelper;
import fuzs.essentialpotions.world.inventory.UnlimitedContainerUtils;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class AlchemyBagItem extends Item implements ForwardingItem, Vanishable {
    public static final int POTION_MAX_STACK_SIZE = 16;
    public static final String TAG_SELECTED = "Selected";

    public AlchemyBagItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> useSelf(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            player.openMenu(this.getMenuProvider(stack));
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private MenuProvider getMenuProvider(ItemStack stack) {
        return new SimpleMenuProvider((containerId, inventory, player) -> {
            SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null);
            return new AlchemyBagMenu(containerId, inventory, container);
        }, stack.getHoverName());
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        SimpleContainer container = ContainerItemHelper.loadItemContainer(itemEntity.getItem(), null);
        UnlimitedContainerUtils.dropContents(itemEntity.level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), container);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player && this.useSelectedItem(player)) {
            ItemStack selectedItem = this.getSelectedItem(stack);
            if (!selectedItem.isEmpty()) {
                ItemStack result = selectedItem.finishUsingItem(level, player);
                this.setSelectedItem(stack, selectedItem);
                if (result != selectedItem) {
                    player.getInventory().add(result);
                }
            }
            return stack;
        } else {
            return super.finishUsingItem(stack, level, livingEntity);
        }
    }

    public boolean useSelectedItem(Player player) {
        return !player.isSecondaryUseActive();
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player && this.useSelectedItem(player)) {
            ItemStack selectedItem = this.getSelectedItem(stack);
            if (selectedItem.isEmpty()) {
                selectedItem.releaseUsing(level, player, timeCharged);
            }
        } else {
            super.releaseUsing(stack, level, livingEntity, timeCharged);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && this.useSelectedItem(player)) {
            ItemStack itemInHand = context.getItemInHand();
            ItemStack selectedItem = this.getSelectedItem(itemInHand);
            if (!selectedItem.isEmpty()) {
                this.setItemInHand(player, context.getHand(), selectedItem);
                BlockHitResult hitResult = ((UseOnContextAccessor) context).essentialpotions$callGetHitResult();
                context = new UseOnContext(context.getPlayer(), context.getHand(), hitResult);
                InteractionResult interactionResult = selectedItem.useOn(context);
                ItemStack result = player.getItemInHand(context.getHand());
                this.setItemInHand(player, context.getHand(), itemInHand);
                this.setSelectedItem(itemInHand, selectedItem);
                if (result != selectedItem) {
                    player.getInventory().add(result);
                }
                return interactionResult;
            } else {
                return InteractionResult.PASS;
            }
        } else {
            return super.useOn(context);
        }
    }

    private void setItemInHand(Player player, InteractionHand interactionHand, ItemStack stack) {
        Inventory inventory = player.getInventory();
        if (interactionHand == InteractionHand.OFF_HAND) {
            inventory.offhand.set(0, stack);
        } else {
            inventory.items.set(inventory.selected, stack);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if (this.useSelectedItem(player)) {

            ItemStack itemInHand = player.getItemInHand(usedHand);
            ItemStack selectedItem = this.getSelectedItem(itemInHand);
            if (!selectedItem.isEmpty()) {

                this.setItemInHand(player, usedHand, selectedItem);
                InteractionResultHolder<ItemStack> result = selectedItem.use(level, player, usedHand);
                this.setItemInHand(player, usedHand, itemInHand);

                this.setSelectedItem(itemInHand, selectedItem);
                if (result.getObject() != selectedItem) {
                    player.getInventory().add(result.getObject());
                }

                if (player.getUseItem() == result.getObject()) {
                    ((LivingEntityAccessor) player).essentialpotions$setUseItem(itemInHand);
                }

                return new InteractionResultHolder<>(result.getResult(), itemInHand);
            } else {

                return InteractionResultHolder.pass(itemInHand);
            }
        } else {

            return this.useSelf(level, player, usedHand);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        ItemStack item = this.getSelectedItem(stack);
        return !item.isEmpty() ? item.getUseDuration() : super.getUseDuration(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        ItemStack item = this.getSelectedItem(stack);
        return !item.isEmpty() ? item.getUseAnimation() : super.getUseAnimation(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        ItemStack item = this.getSelectedItem(stack);
        return !item.isEmpty() ? item.hasFoil() : super.isFoil(stack);
    }

    @Override
    public ItemStack getSelectedItem(ItemStack stack) {
        if (stack.hasTag()) {
            SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null);
            return container.getItem(stack.getTag().getInt(TAG_SELECTED));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean setSelectedItem(ItemStack stack, ItemStack selectedItem) {
        if (this.isAllowedInside(selectedItem) && stack.hasTag()) {
            SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null);
            container.setItem(stack.getTag().getInt(TAG_SELECTED), selectedItem);
            return true;
        }
        return false;
    }

    @Override
    public boolean isFoilSelf(ItemStack stack) {
        return super.isFoil(stack);
    }

    @Override
    public boolean isAllowedInside(ItemStack stack) {
        return true || stack.is(ModRegistry.DRINKABLE_POTIONS_ITEM_TAG);
    }

    //    public boolean canCyclePotions(ItemStack stack) {
//        SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null);
//        int foundItems = 0;
//        for (int i = 0; i < container.getContainerSize(); i++) {
//            if (!container.getItem(i).isEmpty()) foundItems++;
//            if (foundItems > 1) return true;
//        }
//        return false;
//    }
//
//    public void cyclePotionRight(ItemStack stack) {
//        SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null);
//        stack.getOrCreateTagElement()
//    }
}
