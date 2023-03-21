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
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
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
        return InteractionResultHolder.consume(stack);
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
            ItemStack item = this.getRenderItem(stack);
            if (!item.isEmpty()) {
                ItemStack result = item.finishUsingItem(level, livingEntity);
                if (item != result && livingEntity instanceof Player player) {
                    player.getInventory().add(result);
                }
                return stack;
            }
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.isSecondaryUseActive()) {
            ItemStack itemInHand = context.getItemInHand();
            ItemStack item = this.getRenderItem(itemInHand);
            if (!item.isEmpty()) {
                itemInHand = itemInHand.copy();
                InteractionResult interactionResult = item.useOn(context);
                if (interactionResult.consumesAction() && player != null) {
                    ItemStack currentItemInHand = player.getItemInHand(context.getHand());
                    if (!ItemStack.matches(currentItemInHand, itemInHand)) {
                        player.setItemInHand(context.getHand(), itemInHand);
                        player.getInventory().add(currentItemInHand);
                    }
                }
                return interactionResult;
            }
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!player.isSecondaryUseActive()) {
            ItemStack item = this.getRenderItem(player.getItemInHand(usedHand));
            if (!item.isEmpty()) {
                return item.use(level, player, usedHand);
            }
        }
        return this.useSelf(level, player, usedHand);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        ItemStack item = this.getRenderItem(stack);
        return !item.isEmpty() ? item.getUseDuration() : super.getUseDuration(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        ItemStack item = this.getRenderItem(stack);
        return !item.isEmpty() ? item.getUseAnimation() : super.getUseAnimation(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        ItemStack item = this.getRenderItem(stack);
        return !item.isEmpty() ? item.hasFoil() : super.isFoil(stack);
    }

    @Override
    public ItemStack getRenderItem(ItemStack stack) {
        ItemStack other = new ItemStack(Items.POTION);
        PotionUtils.setPotion(other, Potions.LEAPING);
        return other;
    }

    @Override
    public boolean isFoilSelf(ItemStack stack) {
        return super.isFoil(stack);
    }

    public boolean canCyclePotions(ItemStack stack) {
        SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null);
        int foundItems = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty()) foundItems++;
            if (foundItems > 1) return true;
        }
        return false;
    }

    public void cyclePotionRight(ItemStack stack) {
        SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null);
        stack.getOrCreateTagElement()
    }
}
