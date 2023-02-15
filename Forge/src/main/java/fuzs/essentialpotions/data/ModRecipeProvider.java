package fuzs.essentialpotions.data;

import fuzs.essentialpotions.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(ModRegistry.ALCHEMY_BAG_ITEM.get())
                .define('#', Ingredient.of(Items.RABBIT_HIDE, Items.LEATHER))
                .define('@', Items.STRING)
                .define('+', Items.BLAZE_POWDER)
                .pattern("@#@")
                .pattern("#+#")
                .pattern("###")
                .unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
                .save(recipeConsumer);
    }
}
