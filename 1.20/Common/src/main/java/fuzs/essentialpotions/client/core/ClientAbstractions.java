package fuzs.essentialpotions.client.core;

import fuzs.puzzleslib.api.core.v1.ServiceProviderHelper;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;

public interface ClientAbstractions {
    ClientAbstractions INSTANCE = ServiceProviderHelper.load(ClientAbstractions.class);

    BakedModel createCustomRendererBakedModel(BakedModel originalModel);

    BakedModel createForwardingOverridesBakedModel(BakedModel originalModel, ItemOverrides overrides);
}
