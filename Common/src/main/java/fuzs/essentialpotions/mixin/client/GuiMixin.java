package fuzs.essentialpotions.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.essentialpotions.client.handler.CyclingInputHandler;
import fuzs.essentialpotions.client.handler.ForwardingItemCyclingHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
abstract class GuiMixin extends GuiComponent {
    @Shadow
    private int toolHighlightTimer;
    @Shadow
    private ItemStack lastToolHighlight;
    @Unique
    private int essentialpotions$originalToolHighlightTimer;
    @Unique
    private ItemStack essentialpotions$originalLastToolHighlight;

    @Inject(method = "renderSelectedItemName", at = @At("HEAD"))
    public void renderSelectedItemName$0(PoseStack poseStack, CallbackInfo callback) {
        if (ForwardingItemCyclingHandler.toolHighlightTimer > 0 && !ForwardingItemCyclingHandler.lastToolHighlight.isEmpty()) {
            this.essentialpotions$originalToolHighlightTimer = this.toolHighlightTimer;
            this.essentialpotions$originalLastToolHighlight = this.lastToolHighlight;
            this.toolHighlightTimer = ForwardingItemCyclingHandler.toolHighlightTimer;
            this.lastToolHighlight = ForwardingItemCyclingHandler.lastToolHighlight;
        }
    }

    @Inject(method = "renderSelectedItemName", at = @At("TAIL"))
    public void renderSelectedItemName$1(PoseStack poseStack, CallbackInfo callback) {
        if (ForwardingItemCyclingHandler.toolHighlightTimer > 0 && !ForwardingItemCyclingHandler.lastToolHighlight.isEmpty()) {
            this.toolHighlightTimer = this.essentialpotions$originalToolHighlightTimer;
            this.lastToolHighlight = this.essentialpotions$originalLastToolHighlight;
        }
    }
}
