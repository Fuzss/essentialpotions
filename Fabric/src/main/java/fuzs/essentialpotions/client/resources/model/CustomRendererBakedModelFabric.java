package fuzs.essentialpotions.client.resources.model;

import fuzs.essentialpotions.client.renderer.block.model.ForwardingItemOverrides;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomRendererBakedModelFabric extends ForwardingBakedModel {

    public CustomRendererBakedModelFabric(BakedModel originalModel) {
        this.wrapped = originalModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        return ForwardingItemOverrides.copyQuadsNoTint(super.getQuads(state, side, rand));
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }
}
