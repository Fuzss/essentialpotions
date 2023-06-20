package fuzs.essentialpotions.world.item;

import fuzs.essentialpotions.world.inventory.AlchemyBagMenu;
import fuzs.essentialpotions.world.inventory.ContainerItemHelper;
import fuzs.essentialpotions.world.inventory.UnlimitedContainerUtils;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

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
        if (!(livingEntity instanceof Player player) || !player.isSecondaryUseActive()) {
            ItemStack selectedItem = this.getSelectedItem(stack);
            if (!selectedItem.isEmpty()) {
                ItemStack result = selectedItem.finishUsingItem(level, livingEntity);
                SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null);
                container.setItem(stack.getTag().getInt(TAG_SELECTED), selectedItem);
                if (selectedItem != result && livingEntity instanceof Player player) {
                    player.getInventory().add(result);
                }
            }
            return stack;
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.isSecondaryUseActive()) {
            ItemStack itemInHand = context.getItemInHand();
            ItemStack selectedItem = this.getSelectedItem(itemInHand);
            if (!selectedItem.isEmpty()) {
                itemInHand = itemInHand.copy();
                InteractionResult interactionResult = selectedItem.useOn(context);
                if (interactionResult.consumesAction() && player != null) {
                    ItemStack currentItemInHand = player.getItemInHand(context.getHand());
                    if (!ItemStack.matches(currentItemInHand, itemInHand)) {
                        player.setItemInHand(context.getHand(), itemInHand);
                        player.getInventory().add(currentItemInHand);
                    }
                }
                return interactionResult;
            } else {
                return InteractionResult.PASS;
            }
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!player.isSecondaryUseActive()) {
            ItemStack itemInHand = player.getItemInHand(usedHand);
            ItemStack selectedItem = this.getSelectedItem(itemInHand);
            if (!selectedItem.isEmpty()) {
                return selectedItem.use(level, player, usedHand);
            } else {
                return InteractionResultHolder.pass(itemInHand);
            }
        }
        return this.useSelf(level, player, usedHand);
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
    public boolean isFoilSelf(ItemStack stack) {
        return super.isFoil(stack);
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
