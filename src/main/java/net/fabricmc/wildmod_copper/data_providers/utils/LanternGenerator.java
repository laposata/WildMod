package net.fabricmc.wildmod_copper.data_providers.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Consumer;

import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;
import static net.fabricmc.wildmod_copper.registry.BlockRegistry.COPPER_LANTERN;
import static net.fabricmc.wildmod_copper.registry.ItemRegistry.COPPER_NUGGET;
import static net.minecraft.data.server.RecipeProvider.conditionsFromItem;
import static net.minecraft.data.server.RecipeProvider.hasItem;

public class LanternGenerator {
    public static final Model TEMPLATE_UNPOWERED_LANTERN = block("template_unpowered_lantern",TextureKey.LANTERN);
    public static final Model TEMPLATE_HANGING_LANTERN_UNPOWERED = block("template_unpowered_hanging_lantern", "_hanging", TextureKey.LANTERN);


    private static Model block(String parent, TextureKey ... requiredTextureKeys) {
        return new Model(Optional.of(new Identifier(NAMESPACE, "block/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    private static Model block(String parent, String variant, TextureKey ... requiredTextureKeys) {
        return new Model(Optional.of(new Identifier(NAMESPACE, "block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }

    public static TextureMap lantern(Identifier id) {
        return new TextureMap().put(TextureKey.LANTERN, id);
    }


    public static void registerLantern(Block lantern, BlockStateModelGenerator bsmg) {
        TextureMap offTexture = lantern(TextureMap.getSubId(lantern, "_unlit"));

        Identifier lanternOn = TexturedModel.TEMPLATE_LANTERN.upload(lantern, bsmg.modelCollector);
        Identifier lanternOnHanging = TexturedModel.TEMPLATE_HANGING_LANTERN.upload(lantern, bsmg.modelCollector);


        Identifier lanternOff = Models.TEMPLATE_LANTERN.upload(lantern, "_unlit", offTexture, bsmg.modelCollector);
        Identifier lanternOffHanging = Models.TEMPLATE_HANGING_LANTERN.upload(lantern,"_unlit", offTexture, bsmg.modelCollector);

        bsmg.registerItemModel(lantern.asItem());

        bsmg.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(lantern)
                        .coordinate(BlockStateVariantMap.create(Properties.HANGING, Properties.POWERED)
                                .register((hanging, power) -> {
                                    if(power){
                                        return BlockStateVariant.create().put(VariantSettings.MODEL, hanging ? lanternOnHanging: lanternOn);
                                    }
                                    return BlockStateVariant.create().put(VariantSettings.MODEL, hanging ? lanternOffHanging: lanternOff);
                                })
                        )
        );
    }

    public static void copperLanternRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(COPPER_LANTERN)
                .input('N', COPPER_NUGGET)
                .input('R', Items.REDSTONE_TORCH)
                .input('G', Items.GLOWSTONE_DUST)
                .input('Q', Items.QUARTZ)
                .pattern("NGN")
                .pattern("NRN")
                .pattern("NQN")
                .criterion(hasItem(COPPER_NUGGET), RecipeProvider.conditionsFromItem(COPPER_NUGGET))
                .offerTo(exporter);

    }
}
