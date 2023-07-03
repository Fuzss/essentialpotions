package fuzs.essentialpotions.client.core;

import fuzs.essentialpotions.client.resources.model.CustomRendererBakedModelForge;
import fuzs.essentialpotions.client.resources.model.ForwardingOverridesBakedModelForge;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;

public final class ForgeClientAbstractions implements ClientAbstractions {

    @Override
    public BakedModel createCustomRendererBakedModel(BakedModel originalModel) {
        return new CustomRendererBakedModelForge(originalModel);
    }

    @Override
    public BakedModel createForwardingOverridesBakedModel(BakedModel originalModel, ItemOverrides overrides) {
        return new ForwardingOverridesBakedModelForge(originalModel, overrides);
    }
}
