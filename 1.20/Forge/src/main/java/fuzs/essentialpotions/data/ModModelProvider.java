package fuzs.essentialpotions.data;

import fuzs.essentialpotions.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractModelProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void registerStatesAndModels() {
        this.basicItem(this.modLoc("alchemy_bag_model"), ModRegistry.ALCHEMY_BAG_ITEM.get());
        this.builtInItem(ModRegistry.ALCHEMY_BAG_ITEM.get());
    }

    public ItemModelBuilder builtInItem(Item item) {
        return this.builtInItem(ForgeRegistries.ITEMS.getKey(item));
    }

    public ItemModelBuilder builtInItem(ResourceLocation item) {
        return this.itemModels().getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }
}
