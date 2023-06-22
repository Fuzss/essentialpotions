package fuzs.essentialpotions.world.item;

import fuzs.essentialpotions.init.ModRegistry;
import fuzs.essentialpotions.mixin.accessor.LivingEntityAccessor;
import fuzs.essentialpotions.mixin.accessor.UseOnContextAccessor;
import fuzs.essentialpotions.world.inventory.AlchemyBagMenu;
import fuzs.essentialpotions.world.inventory.ContainerItemHelper;
import fuzs.essentialpotions.world.inventory.UnlimitedContainerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AlchemyBagItem extends Item implements ForwardingItem, Vanishable {
    public static final int POTION_MAX_STACK_SIZE = 16;

    public AlchemyBagItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> useSelf(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (!level.isClientSide) {
            player.openMenu(this.getMenuProvider(itemInHand));
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        if (this.getSelectedItem(itemInHand).isEmpty()) {
            return InteractionResultHolder.sidedSuccess(itemInHand, level.isClientSide);
        } else {
            return InteractionResultHolder.consume(itemInHand);
        }
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
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (livingEntity instanceof Player player && this.useSelectedItem(player)) {
            ItemStack selectedItem = this.getSelectedItem(stack);
            if (!selectedItem.isEmpty()) {
                selectedItem.onUseTick(level, livingEntity, remainingUseDuration);
                this.setSelectedItem(stack, selectedItem);
            }
        }
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        if (this.useSelectedItem(player)) {
            ItemStack itemInHand = player.getMainHandItem();
            ItemStack selectedItem = this.getSelectedItem(itemInHand);
            if (!selectedItem.isEmpty()) {
                boolean result = selectedItem.getItem().canAttackBlock(state, level, pos, player);
                this.setSelectedItem(itemInHand, selectedItem);
                return result;
            } else {
                return true;
            }
        }
        return super.canAttackBlock(state, level, pos, player);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        ItemStack item = this.getSelectedItem(stack);
        return !item.isEmpty() ? item.getDestroySpeed(state) : super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && this.useSelectedItem(player)) {
            ItemStack selectedItem = this.getSelectedItem(stack);
            if (!selectedItem.isEmpty()) {
                boolean result = selectedItem.getItem().hurtEnemy(selectedItem, target, player);
                this.setSelectedItem(stack, selectedItem);
                return result;
            } else {
                return false;
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (miningEntity instanceof Player player && this.useSelectedItem(player)) {
            ItemStack selectedItem = this.getSelectedItem(stack);
            if (!selectedItem.isEmpty()) {
                boolean result = selectedItem.getItem().mineBlock(selectedItem, level, state, pos, player);
                this.setSelectedItem(stack, selectedItem);
                return result;
            } else {
                return false;
            }
        }
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (this.useSelectedItem(player)) {
            ItemStack selectedItem = this.getSelectedItem(stack);
            if (!selectedItem.isEmpty()) {
                InteractionResult result = selectedItem.interactLivingEntity(player, interactionTarget, usedHand);
                this.setSelectedItem(stack, selectedItem);
                return result;
            } else {
                return InteractionResult.PASS;
            }
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ItemStack item = this.getSelectedItem(stack);
        if (!item.isEmpty()) {
            item.inventoryTick(level, entity, slotId, isSelected);
        } else {
            super.inventoryTick(stack, level, entity, slotId, isSelected);
        }
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        ItemStack item = this.getSelectedItem(stack);
        return !item.isEmpty() ? item.useOnRelease() : super.useOnRelease(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player && this.useSelectedItem(player)) {
            ItemStack selectedItem = this.getSelectedItem(stack);
            if (!selectedItem.isEmpty()) {
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

                // this would be much better the other way around, keeping the selected item as useItem,
                // but vanilla constantly checks with the actually held item, so it needs to be this item
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
        return stack.is(ModRegistry.DRINKABLE_POTIONS_ITEM_TAG);
    }
}
