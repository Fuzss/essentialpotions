package fuzs.essentialpotions.client.resources.model;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;

public class ForwardingOverridesBakedModelFabric extends ForwardingBakedModel {
    protected final ItemOverrides overrides;

    public ForwardingOverridesBakedModelFabric(BakedModel originalModel, ItemOverrides overrides) {
        this.wrapped = originalModel;
        this.overrides = overrides;
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.overrides;
    }
}
