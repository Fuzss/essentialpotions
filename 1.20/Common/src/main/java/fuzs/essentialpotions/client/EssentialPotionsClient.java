package fuzs.essentialpotions.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.essentialpotions.client.core.ClientAbstractions;
import fuzs.essentialpotions.client.gui.screens.AlchemyBagScreen;
import fuzs.essentialpotions.client.handler.ForwardingCyclingProvider;
import fuzs.essentialpotions.client.handler.ForwardingItemCyclingHandler;
import fuzs.essentialpotions.client.init.ClientModRegistry;
import fuzs.essentialpotions.client.renderer.block.model.ForwardingItemOverrides;
import fuzs.essentialpotions.init.ModRegistry;
import fuzs.essentialpotions.mixin.client.accessor.ItemRendererAccessor;
import fuzs.essentialpotions.world.item.ForwardingItem;
import fuzs.puzzlesapi.api.client.slotcycling.v1.SlotCyclingProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.BuiltinModelItemRendererContext;
import fuzs.puzzleslib.api.client.core.v1.context.ColorProvidersContext;
import fuzs.puzzleslib.api.client.core.v1.context.DynamicModifyBakingResultContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.init.v1.DynamicBuiltinItemRenderer;
import fuzs.puzzleslib.api.client.init.v1.ItemModelDisplayOverrides;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class EssentialPotionsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ClientTickEvents.START.register(ForwardingItemCyclingHandler::onClientTick$Start);
    }

    @Override
    public void onClientSetup() {
        MenuScreens.register(ModRegistry.ALCHEMY_BAG_MENU_TYPE.get(), AlchemyBagScreen::new);
        ItemModelDisplayOverrides.INSTANCE.register(ClientModRegistry.ALCHEMY_BAG_ITEM_MODEL, ClientModRegistry.ALCHEMY_BAG_IN_HAND_ITEM_MODEL, ItemDisplayContext.GUI, ItemDisplayContext.GROUND, ItemDisplayContext.FIXED);
        SlotCyclingProvider.registerProvider(ModRegistry.ALCHEMY_BAG_ITEM.get(), ForwardingCyclingProvider::new);
    }

    @Override
    public void onModifyBakingResult(DynamicModifyBakingResultContext context) {
//        onModifyBakingResult(context.models());
    }

    public static void onModifyBakingResult(Map<ResourceLocation, BakedModel> models) {
        BakedModel bakedModel = models.get(ClientModRegistry.ALCHEMY_BAG_ITEM_MODEL);
        ForwardingItemOverrides forwardingItemOverrides = new ForwardingItemOverrides(bakedModel.getOverrides());
        bakedModel = ClientAbstractions.INSTANCE.createCustomRendererBakedModel(bakedModel);
        BakedModel wrappedBakedModel = ClientAbstractions.INSTANCE.createForwardingOverridesBakedModel(bakedModel, forwardingItemOverrides);
        models.put(ClientModRegistry.ALCHEMY_BAG_ITEM_MODEL, bakedModel);
        models.put(ClientModRegistry.ALCHEMY_BAG_IN_HAND_ITEM_MODEL, wrappedBakedModel);
    }

    @Override
    public void onRegisterBuiltinModelItemRenderers(BuiltinModelItemRendererContext context) {
        context.registerItemRenderer(new DynamicBuiltinItemRenderer() {
            private ItemRenderer itemRenderer;
            private BakedModel alchemyBagModel;

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                this.itemRenderer = Minecraft.getInstance().getItemRenderer();
                ModelManager modelManager = this.itemRenderer.getItemModelShaper().getModelManager();
                this.alchemyBagModel = modelManager.getModel(ClientModRegistry.ALCHEMY_BAG_ITEM_MODEL);
            }

            @Override
            public void renderByItem(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
                RenderType renderType = ItemBlockRenderTypes.getRenderType(stack, false);
                boolean hasFoil = stack.getItem() instanceof ForwardingItem item ? item.isFoilSelf(stack) : stack.hasFoil();
                VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(vertexConsumers, renderType, true, hasFoil);
                ((ItemRendererAccessor) this.itemRenderer).essentialpotions$callRenderModelLists(this.alchemyBagModel, stack, light, overlay, matrices, vertexConsumer);
            }
        }, ModRegistry.ALCHEMY_BAG_ITEM.get());
    }

    @Override
    public void onRegisterItemColorProviders(ColorProvidersContext<Item, ItemColor> context) {
        context.registerColorProvider((itemStack, i) -> {
            if (itemStack.getItem() instanceof ForwardingItem item) {
                ItemStack renderItem = item.getSelectedItem(itemStack);
                if (renderItem.getItem() != ModRegistry.ALCHEMY_BAG_ITEM.get()) {
                    return context.getProvider(renderItem.getItem()).getColor(renderItem, i);
                }
            }
            return -1;
        }, ModRegistry.ALCHEMY_BAG_ITEM.get());
    }
}
