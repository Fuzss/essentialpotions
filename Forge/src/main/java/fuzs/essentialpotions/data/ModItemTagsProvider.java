package fuzs.essentialpotions.data;

import fuzs.essentialpotions.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator dataGenerator, String modId, @Nullable ExistingFileHelper fileHelper) {
        super(dataGenerator, new BlockTagsProvider(dataGenerator, modId, fileHelper), modId, fileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModRegistry.DRINKABLE_POTIONS_ITEM_TAG).add(Items.POTION);
    }
}
