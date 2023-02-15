package fuzs.essentialpotions.client.core;

import fuzs.puzzleslib.util.PuzzlesUtil;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;

public interface ClientAbstractions {
    ClientAbstractions INSTANCE = PuzzlesUtil.loadServiceProvider(ClientAbstractions.class);

    BakedModel createCustomRendererBakedModel(BakedModel originalModel);

    BakedModel createForwardingOverridesBakedModel(BakedModel originalModel, ItemOverrides overrides);
}
