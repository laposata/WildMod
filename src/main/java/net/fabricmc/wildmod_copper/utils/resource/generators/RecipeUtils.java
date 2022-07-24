package net.fabricmc.wildmod_copper.utils.resource.generators;

import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

import java.util.function.Consumer;

import static net.minecraft.data.server.RecipeProvider.conditionsFromItem;
import static net.minecraft.data.server.RecipeProvider.hasItem;

public class RecipeUtils {
    public static CraftingRecipeJsonBuilder createPressurePlateRecipe(ItemConvertible output, Ingredient input, int count) {
        return ShapedRecipeJsonBuilder.create(output, count)
                .input('#', input)
                .pattern("##");
    }

    public static void offerPressurePlateRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, Ingredient input, int count, Block needToGet) {
        createPressurePlateRecipe(output, input, count).criterion(hasItem(needToGet), conditionsFromItem(output)).offerTo(exporter);
    }

}
