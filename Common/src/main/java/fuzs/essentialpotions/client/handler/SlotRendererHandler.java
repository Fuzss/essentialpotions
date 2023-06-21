package fuzs.essentialpotions.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class SlotRendererHandler {
    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    public static void tryRenderSlots(Minecraft minecraft, PoseStack poseStack, float partialTicks, int screenWidth, int screenHeight) {
        if (!minecraft.options.hideGui && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            if (minecraft.getCameraEntity() instanceof Player player) {
                SlotCyclingProvider provider = SlotCyclingProvider.getProvider(player);
                if (provider != null) {
                    ItemStack forwardStack = provider.getForwardStack();
                    ItemStack backwardStack = provider.getBackwardStack();
                    if (!forwardStack.isEmpty() && !backwardStack.isEmpty()) {
                        renderAdditionalSlots(poseStack, partialTicks, screenWidth, screenHeight, minecraft.font, (Player) minecraft.getCameraEntity(), provider);
                    }
                }
            }
        }
    }

    private static void renderAdditionalSlots(PoseStack poseStack, float partialTicks, int screenWidth, int screenHeight, Font font, Player player, SlotCyclingProvider provider) {

        ItemStack selectedStack = provider.getSelectedStack();
        ItemStack forwardStack = provider.getForwardStack();
        ItemStack backwardStack = provider.getBackwardStack();

        if (forwardStack.isEmpty() || backwardStack.isEmpty()) return;

        boolean renderToRight = player.getMainArm().getOpposite() == HumanoidArm.LEFT;

        if (ItemStack.matches(forwardStack, backwardStack)) {
            if (renderToRight) {
                forwardStack = ItemStack.EMPTY;
            } else {
                backwardStack = ItemStack.EMPTY;
            }
        }

        int posX = screenWidth / 2 + (91 + EssentialPotions.CONFIG.get(ClientConfig.class).slotsXOffset) * (renderToRight ? 1 : -1);
        int posY = screenHeight - EssentialPotions.CONFIG.get(ClientConfig.class).slotsYOffset;
        if (EssentialPotions.CONFIG.get(ClientConfig.class).slotsDisplayState == ClientConfig.SlotsDisplayState.KEY) {
            posY += (screenHeight - posY + 23) * (1.0F - Math.min(1.0F, (CyclingInputHandler.getSlotsDisplayTicks() - partialTicks) / 5.0F));
        }

        renderSlotBackgrounds(poseStack, posX, posY, !forwardStack.isEmpty(), !backwardStack.isEmpty(), renderToRight);
        renderSlotItems(partialTicks, posX, posY - (16 + 3), font, player, selectedStack, forwardStack, backwardStack, renderToRight, provider);
    }

    private static void renderSlotBackgrounds(PoseStack poseStack, int posX, int posY, boolean renderForwardStack, boolean renderBackwardStack, boolean renderToRight) {

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

    private static void renderSlotItems(float partialTicks, int posX, int posY, Font font, Player player, ItemStack selectedStack, ItemStack forwardStack, ItemStack backwardStack, boolean renderToRight, SlotCyclingProvider provider) {

        if (renderToRight) {

            renderItemInSlot(font, posX + 10, posY, partialTicks, player, backwardStack, provider);
            renderItemInSlot(font, posX + 10 + 20, posY, partialTicks, player, selectedStack, provider);
            renderItemInSlot(font, posX + 10 + 20 + 20, posY, partialTicks, player, forwardStack, provider);
        } else {

            renderItemInSlot(font, posX - 26, posY, partialTicks, player, forwardStack, provider);
            renderItemInSlot(font, posX - 26 - 20, posY, partialTicks, player, selectedStack, provider);
            renderItemInSlot(font, posX - 26 - 20 - 20, posY, partialTicks, player, backwardStack, provider);
        }
    }

    private static void renderItemInSlot(Font font, int posX, int posY, float tickDelta, Player player, ItemStack stack, SlotCyclingProvider provider) {

        if (!stack.isEmpty()) {

            PoseStack posestack = RenderSystem.getModelViewStack();
            float popTime = CyclingInputHandler.getGlobalPopTime() - tickDelta;
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
