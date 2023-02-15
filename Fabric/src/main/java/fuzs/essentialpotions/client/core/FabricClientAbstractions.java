package fuzs.essentialpotions.client.core;

import fuzs.essentialpotions.client.resources.model.CustomRendererBakedModelFabric;
import fuzs.essentialpotions.client.resources.model.ForwardingOverridesBakedModelFabric;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;

public class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public BakedModel createCustomRendererBakedModel(BakedModel originalModel) {
        return new CustomRendererBakedModelFabric(originalModel);
    }

    @Override
    public BakedModel createForwardingOverridesBakedModel(BakedModel originalModel, ItemOverrides overrides) {
        return new ForwardingOverridesBakedModelFabric(originalModel, overrides);
    }
}
