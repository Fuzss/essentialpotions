package fuzs.essentialpotions.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.config.ClientConfig;
import fuzs.essentialpotions.init.ModRegistry;
import fuzs.essentialpotions.network.ServerboundCyclePotionMessage;
import fuzs.essentialpotions.world.inventory.AlchemyBagMenu;
import fuzs.essentialpotions.world.inventory.ContainerItemHelper;
import fuzs.essentialpotions.world.item.AlchemyBagItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

import java.util.Collections;
import java.util.List;

public class SlotRendererHandler {
    public static final SlotRendererHandler INSTANCE = new SlotRendererHandler();
    private static final List<SlotRendererHandler> SLOT_RENDERER_HANDLERS = Collections.singletonList(INSTANCE);
    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    public static void tryRenderSlots(Minecraft minecraft, PoseStack poseStack, float partialTicks, int screenWidth, int screenHeight) {
        for (SlotRendererHandler slotRendererHandler : SLOT_RENDERER_HANDLERS) {
            if (slotRendererHandler.isAllowedToRender(minecraft)) {
                slotRendererHandler.render(poseStack, partialTicks, screenWidth, screenHeight, minecraft.font, (Player) minecraft.getCameraEntity());
                return;
            }
        }
    }

    private boolean isAllowedToRender(Minecraft minecraft) {
        if (!minecraft.options.hideGui && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR)
            if (minecraft.getCameraEntity() instanceof Player player) {
                Inventory inventory = player.getInventory();
                if (this.supportsSelectedItem(inventory.getSelected())) {
                    ItemStack forwardStack = this.getForwardStack(inventory);
                    ItemStack backwardStack = this.getBackwardStack(inventory);
                    return !forwardStack.isEmpty() && !backwardStack.isEmpty();
                }
            }
        return false;
    }

    public boolean supportsSelectedItem(ItemStack stack) {
        return stack.is(ModRegistry.ALCHEMY_BAG_ITEM.get());
    }

    public ItemStack getSelectedStack(Inventory inventory) {
        ItemStack stack = inventory.getSelected();
        if (stack.hasTag()) {
            int selected = stack.getTag().getInt(AlchemyBagItem.TAG_SELECTED);
            SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null, false);
            return container.getItem(selected);
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getForwardStack(Inventory inventory) {
        ItemStack stack = inventory.getSelected();
        if (stack.hasTag()) {
            int selected = stack.getTag().getInt(AlchemyBagItem.TAG_SELECTED);
            SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null, false);
            for (int i = 1; i < AlchemyBagMenu.ALCHEMY_BAG_SLOTS; i++) {
                int slot = (selected + i + AlchemyBagMenu.ALCHEMY_BAG_SLOTS) % AlchemyBagMenu.ALCHEMY_BAG_SLOTS;
                ItemStack stackInSlot = container.getItem(slot);
                if (!stackInSlot.isEmpty()) {
                    return stackInSlot;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getBackwardStack(Inventory inventory) {
        ItemStack stack = inventory.getSelected();
        if (stack.hasTag()) {
            int selected = stack.getTag().getInt(AlchemyBagItem.TAG_SELECTED);
            SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null, false);
            for (int i = 1; i < AlchemyBagMenu.ALCHEMY_BAG_SLOTS; i++) {
                int slot = (selected - i + AlchemyBagMenu.ALCHEMY_BAG_SLOTS) % AlchemyBagMenu.ALCHEMY_BAG_SLOTS;
                ItemStack stackInSlot = container.getItem(slot);
                if (!stackInSlot.isEmpty()) {
                    return stackInSlot;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public int getForwardSlot(Inventory inventory) {
        ItemStack stack = inventory.getSelected();
        if (stack.hasTag()) {
            int selected = stack.getTag().getInt(AlchemyBagItem.TAG_SELECTED);
            SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null, false);
            for (int i = 1; i < AlchemyBagMenu.ALCHEMY_BAG_SLOTS; i++) {
                int slot = (selected + i + AlchemyBagMenu.ALCHEMY_BAG_SLOTS) % AlchemyBagMenu.ALCHEMY_BAG_SLOTS;
                if (!container.getItem(slot).isEmpty()) {
                    return slot;
                }
            }
        }
        return -1;
    }

    public int getBackwardSlot(Inventory inventory) {
        ItemStack stack = inventory.getSelected();
        if (stack.hasTag()) {
            int selected = stack.getTag().getInt(AlchemyBagItem.TAG_SELECTED);
            SimpleContainer container = ContainerItemHelper.loadItemContainer(stack, null, false);
            for (int i = 1; i < AlchemyBagMenu.ALCHEMY_BAG_SLOTS; i++) {
                int slot = (selected - i + AlchemyBagMenu.ALCHEMY_BAG_SLOTS) % AlchemyBagMenu.ALCHEMY_BAG_SLOTS;
                if (!container.getItem(slot).isEmpty()) {
                    return slot;
                }
            }
        }
        return -1;
    }

    public boolean cycleSlotForward(Inventory inventory, InteractionHand interactionHand) {
        if (!this.getForwardStack(inventory).isEmpty()) {
            ServerboundCyclePotionMessage.setSelectedItem();
            EssentialPotions.NETWORK.sendToServer(new ServerboundCyclePotionMessage(inventory.selected, interactionHand, true));
            return true;
        }
        return false;
    }

    public boolean cycleSlotBackward(Inventory inventory, InteractionHand interactionHand) {
        if (!this.getForwardStack(inventory).isEmpty()) {
            EssentialPotions.NETWORK.sendToServer(new ServerboundCyclePotionMessage(inventory.selected, interactionHand, false));
            return true;
        }
        return false;
    }

    private void render(PoseStack poseStack, float partialTicks, int screenWidth, int screenHeight, Font font, Player player) {

        ItemStack selectedStack = this.getSelectedStack(player.getInventory());
        ItemStack forwardStack = this.getForwardStack(player.getInventory());
        ItemStack backwardStack = this.getBackwardStack(player.getInventory());

        if (forwardStack.isEmpty() || backwardStack.isEmpty()) return;

        boolean renderToRight = player.getMainArm().getOpposite() == HumanoidArm.LEFT;

        if (forwardStack == backwardStack) {
            if (renderToRight) {
                forwardStack = ItemStack.EMPTY;
            } else {
                backwardStack = ItemStack.EMPTY;
            }
        }

        int posX = screenWidth / 2 + 91 * (renderToRight ? 1 : -1);
        int posY = screenHeight;
        posY -= EssentialPotions.CONFIG.get(ClientConfig.class).slotsOffset;

        this.renderSlotBackgrounds(poseStack, posX, posY, !forwardStack.isEmpty(), !backwardStack.isEmpty(), renderToRight);
        this.renderSlotItems(partialTicks, posX, posY - (16 + 3), font, player, selectedStack, forwardStack, backwardStack, renderToRight);
    }

    private void renderSlotBackgrounds(PoseStack poseStack, int posX, int posY, boolean renderForwardStack, boolean renderBackwardStack, boolean renderToRight) {

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);

        if (renderToRight) {
            GuiComponent.blit(poseStack, posX, posY - 23, 53, 22, 29, 24, 256, 256);
            if (renderForwardStack) {
                GuiComponent.blit(poseStack, posX + 40, posY - 23, 53, 22, 29, 24, 256, 256);
            }
            GuiComponent.blit(poseStack, posX + 28, posY - 22, 21, 0, 20, 22, 256, 256);
            GuiComponent.blit(poseStack, posX + 26, posY - 22 - 1, 0, 22, 24, 24, 256, 256);
        } else {
            if (renderBackwardStack) {
                GuiComponent.blit(poseStack, posX - 29 - 40, posY - 23, 24, 22, 29, 24, 256, 256);
            }
            GuiComponent.blit(poseStack, posX - 29, posY - 23, 24, 22, 29, 24, 256, 256);
            GuiComponent.blit(poseStack, posX - 29 - 19, posY - 22, 21, 0, 20, 22, 256, 256);
            GuiComponent.blit(poseStack, posX - 29 - 21, posY - 22 - 1, 0, 22, 24, 24, 256, 256);
        }
    }

    private void renderSlotItems(float partialTicks, int posX, int posY, Font font, Player player, ItemStack selectedStack, ItemStack forwardStack, ItemStack backwardStack, boolean renderToRight) {

        if (renderToRight) {

            renderItemInSlot(font, posX + 10, posY, partialTicks, player, backwardStack);
            renderItemInSlot(font, posX + 10 + 20, posY, partialTicks, player, selectedStack);
            renderItemInSlot(font, posX + 10 + 20 + 20, posY, partialTicks, player, forwardStack);
        } else {

            renderItemInSlot(font, posX - 26, posY, partialTicks, player, forwardStack);
            renderItemInSlot(font, posX - 26 - 20, posY, partialTicks, player, selectedStack);
            renderItemInSlot(font, posX - 26 - 20 - 20, posY, partialTicks, player, backwardStack);
        }
    }

    private static void renderItemInSlot(Font font, int posX, int posY, float tickDelta, Player player, ItemStack stack) {

        if (!stack.isEmpty()) {

            PoseStack posestack = RenderSystem.getModelViewStack();
            float popTime = Math.max(stack.getPopTime(), KeyBindingHandler.globalPopTime) - tickDelta;
            if (popTime > 0.0F) {
                float f1 = 1.0F + popTime / 5.0F;
                posestack.pushPose();
                posestack.translate(posX + 8, posY + 12, 0.0D);
                posestack.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                posestack.translate(-(posX + 8), -(posY + 12), 0.0D);
                RenderSystem.applyModelViewMatrix();
            }

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderAndDecorateItem(player, stack, posX, posY, 0);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            if (popTime > 0.0F) {
                posestack.popPose();
                RenderSystem.applyModelViewMatrix();
            }
            itemRenderer.renderGuiItemDecorations(font, stack, posX, posY);
        }
    }
}
