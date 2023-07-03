package fuzs.essentialpotions.data;

import fuzs.essentialpotions.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.function.Consumer;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.ALCHEMY_BAG_ITEM.get())
                .define('#', Ingredient.of(Items.RABBIT_HIDE, Items.LEATHER))
                .define('@', Items.STRING)
                .define('+', Items.BLAZE_POWDER)
                .pattern("@#@")
                .pattern("#+#")
                .pattern("###")
                .unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
                .save(exporter);
    }
}
