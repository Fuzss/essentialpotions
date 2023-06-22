package fuzs.essentialpotions.client;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.client.handler.CyclingInputHandler;
import fuzs.essentialpotions.client.handler.ForwardingItemCyclingHandler;
import fuzs.essentialpotions.client.handler.SlotsRendererHandler;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = EssentialPotions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EssentialPotionsForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientFactories.INSTANCE.clientModConstructor(EssentialPotions.MOD_ID).accept(new EssentialPotionsClient());
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final RenderGuiEvent.Post evt) -> {
            SlotsRendererHandler.tryRenderSlots(Minecraft.getInstance(), evt.getPoseStack(), evt.getPartialTick(), evt.getWindow().getGuiScaledWidth(), evt.getWindow().getGuiScaledHeight());
        });
        MinecraftForge.EVENT_BUS.addListener((final TickEvent.ClientTickEvent evt) -> {
            if (evt.phase != TickEvent.Phase.START) return;
            CyclingInputHandler.onClientTick$Start(Minecraft.getInstance());
        });
        MinecraftForge.EVENT_BUS.addListener((final TickEvent.ClientTickEvent evt) -> {
            if (evt.phase != TickEvent.Phase.START) return;
            ForwardingItemCyclingHandler.onClientTick$Start(Minecraft.getInstance());
        });
        MinecraftForge.EVENT_BUS.addListener((final InputEvent.MouseScrollingEvent evt) -> {
            CyclingInputHandler.onBeforeMouseScroll(evt.isLeftDown(), evt.isMiddleDown(), evt.isRightDown(), evt.getScrollDelta(), evt.getScrollDelta()).ifPresent(unit -> evt.setCanceled(true));
        });
    }
}
