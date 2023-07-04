package fuzs.essentialpotions.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.mixin.client.accessor.ItemRendererAccessor;
import fuzs.essentialpotions.world.item.ForwardingItem;
import fuzs.puzzleslib.api.client.init.v1.DynamicBuiltinItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class AlchemyBagItemRenderer implements DynamicBuiltinItemRenderer {
    public static final ModelResourceLocation ALCHEMY_BAG_ITEM_MODEL = new ModelResourceLocation(EssentialPotions.id("alchemy_bag_model"), "inventory");

    private ItemRenderer itemRenderer;
    private BakedModel alchemyBagModel;

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
        ModelManager modelManager = this.itemRenderer.getItemModelShaper().getModelManager();
        this.alchemyBagModel = modelManager.getModel(ALCHEMY_BAG_ITEM_MODEL);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        ItemStack stackToRender = this.getStackToRender(stack, mode);
        BakedModel bakedModel;
        if (stack != stackToRender) {
            bakedModel = this.itemRenderer.getModel(stackToRender, null, null, 0);
        } else {
            bakedModel = this.alchemyBagModel;
        }
        RenderType renderType = ItemBlockRenderTypes.getRenderType(stackToRender, false);
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(vertexConsumers, renderType, true, stackToRender.hasFoil());
        ((ItemRendererAccessor) this.itemRenderer).essentialpotions$callRenderModelLists(bakedModel, stackToRender, light, overlay, matrices, vertexConsumer);
    }

    private ItemStack getStackToRender(ItemStack stack, ItemDisplayContext mode) {
        if (mode != ItemDisplayContext.GUI && mode != ItemDisplayContext.GROUND && mode != ItemDisplayContext.FIXED) {
            ItemStack selectedItem = ((ForwardingItem) stack.getItem()).getSelectedItem(stack);
            if (!selectedItem.isEmpty()) {
                return selectedItem;
            }
        }
        return stack;
    }
}
