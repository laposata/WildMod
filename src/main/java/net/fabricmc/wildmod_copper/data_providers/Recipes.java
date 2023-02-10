package net.fabricmc.wildmod_copper.data_providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.wildmod_copper.data_providers.collections.BlockTags;
import net.fabricmc.wildmod_copper.data_providers.utils.LanternGenerator;
import net.fabricmc.wildmod_copper.utils.resource.generators.RecipeUtils;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import java.util.function.Consumer;

import static net.fabricmc.wildmod_copper.data_providers.collections.ItemTags.*;
import static net.fabricmc.wildmod_copper.registry.BlockRegistry.*;
import static net.fabricmc.wildmod_copper.registry.ItemRegistry.COPPER_NUGGET;
import static net.minecraft.block.Blocks.*;

public class Recipes extends FabricRecipeProvider {
    public Recipes(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        stoneCutterRecipe(exporter);
        buttonCrafting(exporter);
        plateCrafting(exporter);
        LanternGenerator.copperLanternRecipe(exporter);

        RecipeProvider.offerReversibleCompactingRecipesWithCompactingRecipeGroup(exporter,
                COPPER_NUGGET,
                Items.COPPER_INGOT,
                "copper_ingot_from_nuggets",
                "copper_ingot");
    }
    public void stoneCutterRecipe(Consumer<RecipeJsonProvider> exporter){
        SingleItemRecipeJsonBuilder.createStonecutting(
                Ingredient.fromTag(COPPERS_ITEM),
                COPPER_BUTTON
        ).criterion(hasItem(COPPER_BLOCK), conditionsFromTag(COPPERS_ITEM))
                .offerTo(exporter, "copper_button_from_stonecutter");
        SingleItemRecipeJsonBuilder.createStonecutting(
                Ingredient.fromTag(WEATHERED_COPPERS_ITEM),
                WEATHERED_COPPER_BUTTON
        ).criterion(hasItem(WEATHERED_COPPER), conditionsFromTag(WEATHERED_COPPERS_ITEM))
                .offerTo(exporter, "weathered_copper_button_from_stonecutter");
        SingleItemRecipeJsonBuilder.createStonecutting(
                Ingredient.fromTag(EXPOSED_COPPERS_ITEM),
                EXPOSED_COPPER_BUTTON
        ).criterion(hasItem(EXPOSED_COPPER), conditionsFromTag(EXPOSED_COPPERS_ITEM))
                .offerTo(exporter, "exposed_copper_button_from_stonecutter");
        SingleItemRecipeJsonBuilder.createStonecutting(
                Ingredient.fromTag(OXIDIZED_COPPERS_ITEM),
                OXIDIZED_COPPER_BUTTON
        ).criterion(hasItem(OXIDIZED_COPPER), conditionsFromTag(OXIDIZED_COPPERS_ITEM))
                .offerTo(exporter, "oxidized_copper_button_from_stonecutter");
    }
    public void buttonCrafting(Consumer<RecipeJsonProvider> exporter){
        ShapelessRecipeJsonBuilder.create(
                COPPER_BUTTON,
                9
        ).criterion(hasItem(COPPER_BLOCK), conditionsFromTag(COPPERS_ITEM))
                .input(Ingredient.ofItems(CUT_COPPER, WAXED_CUT_COPPER))
                .offerTo(exporter, "copper_button");
        ShapelessRecipeJsonBuilder.create(
                WEATHERED_COPPER_BUTTON,
                9
        ).criterion(hasItem(WEATHERED_COPPER), conditionsFromTag(WEATHERED_COPPERS_ITEM))
                .input(Ingredient.fromTag(WEATHERED_COPPERS_ITEM))
                .offerTo(exporter, "weathered_copper_button");
        ShapelessRecipeJsonBuilder.create(
                        EXPOSED_COPPER_BUTTON,
                        9
        ).criterion(hasItem(EXPOSED_COPPER), conditionsFromTag(EXPOSED_COPPERS_ITEM))
                .input(Ingredient.fromTag(EXPOSED_COPPERS_ITEM))
                .offerTo(exporter, "exposed_copper_button");
        ShapelessRecipeJsonBuilder.create(
                        OXIDIZED_COPPER_BUTTON,
                        9
                ).criterion(hasItem(OXIDIZED_COPPER), conditionsFromTag(OXIDIZED_COPPERS_ITEM))
                .input(Ingredient.fromTag(OXIDIZED_COPPERS_ITEM))
                .offerTo(exporter, "oxidized_copper_button");
    }

    public void plateCrafting(Consumer<RecipeJsonProvider> exporter){
        RecipeUtils.offerPressurePlateRecipe(exporter,
                COPPER_PRESSURE_PLATE,
                Ingredient.ofItems(BlockTags.coppers().toArray(ItemConvertible[]::new)),
                9,
                COPPER_BLOCK);
        RecipeUtils.offerPressurePlateRecipe(exporter,
                EXPOSED_COPPER_PRESSURE_PLATE,
                Ingredient.ofItems(BlockTags.exposedCoppers().toArray(ItemConvertible[]::new)),
                9,
                EXPOSED_COPPER);
        RecipeUtils.offerPressurePlateRecipe(exporter,
                OXIDIZED_COPPER_PRESSURE_PLATE,
                Ingredient.ofItems(BlockTags.oxidizedCoppers().toArray(ItemConvertible[]::new)),
                9,
                OXIDIZED_COPPER);
        RecipeUtils.offerPressurePlateRecipe(exporter,
                WEATHERED_COPPER_PRESSURE_PLATE,
                Ingredient.ofItems(BlockTags.weatheredCoppers().toArray(ItemConvertible[]::new)),
                9,
                WEATHERED_COPPER);
    }
}
