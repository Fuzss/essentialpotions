package fuzs.essentialpotions.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.world.inventory.AlchemyBagMenu;
import fuzs.puzzleslib.client.core.ClientAbstractions;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlchemyBagScreen extends UnlimitedContainerScreen<AlchemyBagMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = EssentialPotions.id("textures/gui/container/alchemy_bag.png");

    public AlchemyBagScreen(AlchemyBagMenu chestMenu, Inventory inventory, Component component) {
        super(chestMenu, inventory, component);
        this.passEvents = false;
        this.imageHeight = 137;
        this.titleLabelY = 7;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 0xCFCFCF);
        this.font.draw(poseStack, this.playerInventoryTitle, (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected boolean checkHotbarKeyPressed(int keyCode, int scanCode) {
        // prevent number keys from extracting items from a locked slot
        // vanilla only checks the hovered slot for being accessible, but the hotbar item is directly taken from the inventory, not from a slot,
        // therefore ignoring all restrictions put on the corresponding slot in the menu
        // also the hotbar slot has a varying index as the player inventory is always added last, so we store the first hotbar slot during menu construction
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null) {
            for (int i = 0; i < 9; ++i) {
                if (ClientAbstractions.INSTANCE.isKeyActiveAndMatches(this.minecraft.options.keyHotbarSlots[i], keyCode, scanCode)) {
                    if (!this.menu.getSlot(AlchemyBagMenu.HOTBAR_SLOTS_START + i).allowModification(this.minecraft.player)) {
                        return true;
                    }
                }
            }
        }

        return super.checkHotbarKeyPressed(keyCode, scanCode);
    }
}
