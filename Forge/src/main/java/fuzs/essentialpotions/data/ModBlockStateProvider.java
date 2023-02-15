package fuzs.essentialpotions.data;

import fuzs.essentialpotions.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator dataGenerator, String modId, ExistingFileHelper fileHelper) {
        super(dataGenerator, modId, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.basicItem(ModRegistry.ALCHEMY_BAG_ITEM.get());
        this.basicItem2(ModRegistry.ALCHEMY_BAG_ITEM2.get());
    }

    public ItemModelBuilder basicItem(Item item)
    {
        return basicItem(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
    }

    public ItemModelBuilder basicItem(ResourceLocation item)
    {
        return this.itemModels().getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()))
                .texture("layer1", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()))
                .texture("layer2", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()))
                .texture("layer3", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()))
                .texture("layer4", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }

    public ItemModelBuilder basicItem2(Item item)
    {
        return basicItem2(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
    }

    public ItemModelBuilder basicItem2(ResourceLocation item) {
        return this.itemModels().getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation(item.getNamespace(), "item/alchemy_bag"));
    }
}
