package fuzs.essentialpotions.world.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

import java.util.stream.Stream;

public class AlchemyBagItem extends Item implements ForwardingItem, Vanishable {

    public AlchemyBagItem(Properties properties) {
        super(properties);
    }

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        ItemStack stack = player.getItemInHand(hand);
//        if (!level.isClientSide) {
//            player.openMenu(this.getMenuProvider(stack));
//            player.awardStat(Stats.ITEM_USED.get(this));
//        }
//        return InteractionResultHolder.consume(stack);
//    }
//
//    private MenuProvider getMenuProvider(ItemStack stack) {
//        return new SimpleMenuProvider((containerId, inventory, player) -> {
//            SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null, 1);
//            return new BagItemMenu(this.type, containerId, inventory, container);
//        }, stack.getHoverName());
//    }
//
//    @Override
//    public void onDestroyed(ItemEntity itemEntity) {
//        Stream.Builder<ItemStack> builder = Stream.builder();
//        SimpleContainer container = ContainerItemHelper.loadItemContainer(itemEntity.getItem(), null, 1);
//        for (int i = 0; i < container.getContainerSize(); i++) {
//            ItemStack stack = container.getItem(i);
//            if (!stack.isEmpty()) {
//                builder.add(stack);
//            }
//        }
//        ItemUtils.onContainerDestroyed(itemEntity, builder.build());
//    }

    @Override
    public ItemStack getRenderItem(ItemStack stack) {
//        if (true) return ItemStack.EMPTY;
        ItemStack stack1 = new ItemStack(Items.POTION);
        PotionUtils.setPotion(stack1, Potions.LEAPING);
        return stack1;
    }

    @Override
    public boolean isFoilSelf(ItemStack stack) {
        return super.isFoil(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.getRenderItem(stack).hasFoil();
    }
}
