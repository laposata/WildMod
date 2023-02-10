package net.fabricmc.wildmod_copper;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.wildmod_copper.data_providers.LootTables;
import net.fabricmc.wildmod_copper.data_providers.ModelsProvider;
import net.fabricmc.wildmod_copper.data_providers.Recipes;
import net.fabricmc.wildmod_copper.data_providers.Tags;
import net.fabricmc.wildmod_copper.registry.BlockRegistry;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(LootTables::new);
        fabricDataGenerator.addProvider(Tags.Blocks::new);
        fabricDataGenerator.addProvider(Tags.Items::new);
        fabricDataGenerator.addProvider(ModelsProvider::new);
        fabricDataGenerator.addProvider(Recipes::new);
    }
}
