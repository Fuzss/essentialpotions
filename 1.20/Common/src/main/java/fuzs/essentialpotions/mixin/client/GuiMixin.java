package fuzs.essentialpotions.mixin.client;

import fuzs.essentialpotions.client.handler.ForwardingItemCyclingHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
abstract class GuiMixin {
    @Shadow
    private int toolHighlightTimer;
    @Shadow
    private ItemStack lastToolHighlight;
    @Unique
    private int essentialpotions$originalToolHighlightTimer;
    @Unique
    private ItemStack essentialpotions$originalLastToolHighlight;

    @Inject(method = "renderSelectedItemName", at = @At("HEAD"))
    public void renderSelectedItemName$0(GuiGraphics guiGraphics, CallbackInfo callback) {
        if (ForwardingItemCyclingHandler.getToolHighlightTimer() > 0 && !ForwardingItemCyclingHandler.getLastToolHighlight().isEmpty()) {
            this.essentialpotions$originalToolHighlightTimer = this.toolHighlightTimer;
            this.essentialpotions$originalLastToolHighlight = this.lastToolHighlight;
            this.toolHighlightTimer = ForwardingItemCyclingHandler.getToolHighlightTimer();
            this.lastToolHighlight = ForwardingItemCyclingHandler.getLastToolHighlight();
        }
    }

    @Inject(method = "renderSelectedItemName", at = @At("TAIL"))
    public void renderSelectedItemName$1(GuiGraphics guiGraphics, CallbackInfo callback) {
        if (ForwardingItemCyclingHandler.getToolHighlightTimer() > 0 && !ForwardingItemCyclingHandler.getLastToolHighlight().isEmpty()) {
            this.toolHighlightTimer = this.essentialpotions$originalToolHighlightTimer;
            this.lastToolHighlight = this.essentialpotions$originalLastToolHighlight;
        }
    }
}
