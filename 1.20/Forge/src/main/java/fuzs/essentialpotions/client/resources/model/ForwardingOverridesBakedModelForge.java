package fuzs.essentialpotions.client.resources.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.BakedModelWrapper;

public class ForwardingOverridesBakedModelForge extends BakedModelWrapper<BakedModel> {
    protected final ItemOverrides overrides;

    public ForwardingOverridesBakedModelForge(BakedModel originalModel, ItemOverrides overrides) {
        super(originalModel);
        this.overrides = overrides;
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.overrides;
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext itemDisplayContext, PoseStack poseStack, boolean applyLeftHandTransform) {
        super.applyTransform(itemDisplayContext, poseStack, applyLeftHandTransform);
        return this;
    }
}
