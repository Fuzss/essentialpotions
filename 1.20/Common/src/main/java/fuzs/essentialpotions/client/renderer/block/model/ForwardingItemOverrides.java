package fuzs.essentialpotions.client.renderer.block.model;

import fuzs.essentialpotions.world.item.ForwardingItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class ForwardingItemOverrides extends ItemOverrides {
    private final ItemOverrides overrides;
    private final ItemRenderer itemRenderer;

    public ForwardingItemOverrides(ItemOverrides overrides) {
        super(null, null, List.of());
        this.overrides = overrides;
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Nullable
    @Override
    public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (stack.getItem() instanceof ForwardingItem item) {
            ItemStack selectedItem = item.getSelectedItem(stack);
            if (!selectedItem.isEmpty()) {
                return this.itemRenderer.getModel(selectedItem, level, entity, seed);
            }
        }
        return this.overrides.resolve(model, stack, level, entity, seed);
    }

    public static List<BakedQuad> copyQuadsNoTint(List<BakedQuad> bakedQuads) {
        return bakedQuads.stream().map(ForwardingItemOverrides::copyQuadNoTint).collect(Collectors.toList());
    }

    private static BakedQuad copyQuadNoTint(BakedQuad bakedQuad) {
        return new BakedQuad(bakedQuad.getVertices(), -1, bakedQuad.getDirection(), bakedQuad.getSprite(), bakedQuad.isShade());
    }
}
