package net.fabricmc.wildmod_copper.data_providers;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(LootTables::new);
        fabricDataGenerator.addProvider(Recipes::new);
        fabricDataGenerator.addProvider(Models::new);
        fabricDataGenerator.addProvider(Tags.Blocks::new);
        fabricDataGenerator.addProvider(Tags.Items::new);
    }
}
