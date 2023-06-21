package fuzs.essentialpotions.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.client.core.ClientAbstractions;
import fuzs.essentialpotions.client.gui.screens.AlchemyBagScreen;
import fuzs.essentialpotions.client.handler.AlchemyBagCyclingProvider;
import fuzs.essentialpotions.client.handler.SlotCyclingProvider;
import fuzs.essentialpotions.client.init.ClientModRegistry;
import fuzs.essentialpotions.client.renderer.block.model.ForwardingItemOverrides;
import fuzs.essentialpotions.init.ModRegistry;
import fuzs.essentialpotions.mixin.client.accessor.ItemRendererAccessor;
import fuzs.essentialpotions.world.item.ForwardingItem;
import fuzs.puzzleslib.api.client.renderer.item.v1.ItemModelOverrides;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import fuzs.puzzleslib.core.ModConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EssentialPotionsClient implements ClientModConstructor {
    public static final ModelResourceLocation ALCHEMY_BAG_ITEM_MODEL = new ModelResourceLocation(EssentialPotions.id("alchemy_bag"), "inventory");
    public static final ModelResourceLocation ALCHEMY_BAG_IN_HAND_ITEM_MODEL = new ModelResourceLocation(EssentialPotions.id("alchemy_bag_in_hand"), "inventory");

    @Override
    public void onRegisterMenuScreens(MenuScreensContext context) {
        context.registerMenuScreen(ModRegistry.ALCHEMY_BAG_MENU_TYPE.get(), AlchemyBagScreen::new);
    }

    @Override
    public void onClientSetup(ModConstructor.ModLifecycleContext context) {
        ItemModelOverrides.INSTANCE.register(ModRegistry.ALCHEMY_BAG_ITEM.get(), ALCHEMY_BAG_ITEM_MODEL, ALCHEMY_BAG_IN_HAND_ITEM_MODEL, ItemTransforms.TransformType.GUI, ItemTransforms.TransformType.GROUND, ItemTransforms.TransformType.FIXED);
        SlotCyclingProvider.registerProvider(ModRegistry.ALCHEMY_BAG_ITEM.get(), AlchemyBagCyclingProvider::new);
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMappings(ClientModRegistry.CYCLE_LEFT_KEY_MAPPING, ClientModRegistry.CYCLE_RIGHT_KEY_MAPPING);
    }

    @Override
    public void onRegisterModelBakingCompletedListeners(ModelBakingCompletedListenersContext context) {
        context.registerReloadListener(context1 -> {
            BakedModel bakedModel = context1.models.get(ALCHEMY_BAG_ITEM_MODEL);
            ForwardingItemOverrides forwardingItemOverrides = new ForwardingItemOverrides(context1.modelBakery, bakedModel.getOverrides());
            bakedModel = ClientAbstractions.INSTANCE.createCustomRendererBakedModel(bakedModel);
            BakedModel wrappedBakedModel = ClientAbstractions.INSTANCE.createForwardingOverridesBakedModel(bakedModel, forwardingItemOverrides);
            context1.models.put(ALCHEMY_BAG_ITEM_MODEL, bakedModel);
            context1.models.put(ALCHEMY_BAG_IN_HAND_ITEM_MODEL, wrappedBakedModel);
        });
    }

    @Override
    public void onRegisterBuiltinModelItemRenderers(BuiltinModelItemRendererContext context) {
        context.registerItemRenderer(ModRegistry.ALCHEMY_BAG_ITEM.get(), (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            RenderType renderType = ItemBlockRenderTypes.getRenderType(stack, false);
            boolean hasFoil = stack.getItem() instanceof ForwardingItem item ? item.isFoilSelf(stack) : stack.hasFoil();
            VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(vertexConsumers, renderType, true, hasFoil);
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemModelShaper itemModelShaper = itemRenderer.getItemModelShaper();
            BakedModel model = itemModelShaper.getModelManager().getModel(ALCHEMY_BAG_ITEM_MODEL);
            ((ItemRendererAccessor) itemRenderer).essentialpotions$callRenderModelLists(model, stack, light, overlay, matrices, vertexConsumer);
        });
    }

    @Override
    public void onRegisterItemColorProviders(ColorProvidersContext<Item, ItemColor> context) {
        context.registerColorProvider((itemStack, i) -> {
            if (itemStack.getItem() instanceof ForwardingItem item) {
                ItemStack renderItem = item.getSelectedItem(itemStack);
                if (renderItem.getItem() != ModRegistry.ALCHEMY_BAG_ITEM.get()) {
                    return context.getProviders().getColor(renderItem, i);
                }
            }
            return -1;
        }, ModRegistry.ALCHEMY_BAG_ITEM.get());
    }
}
