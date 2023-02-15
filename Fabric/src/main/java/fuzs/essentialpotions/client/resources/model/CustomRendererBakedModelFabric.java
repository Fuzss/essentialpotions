package fuzs.essentialpotions.client.resources.model;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CustomRendererBakedModelFabric extends ForwardingBakedModel {

    public CustomRendererBakedModelFabric(BakedModel originalModel) {
        this.wrapped = originalModel;
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState blockState, Direction face, RandomSource rand) {
        return super.getQuads(blockState, face, rand);
    }
}
