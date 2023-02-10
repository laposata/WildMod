package net.fabricmc.wildmod_copper.data_providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.wildmod_copper.data_providers.collections.BlockTags;
import net.minecraft.block.Block;

import java.util.List;

import static net.fabricmc.wildmod_copper.registry.BlockRegistry.*;

public class LootTables extends FabricBlockLootTableProvider {

    public LootTables(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateBlockLootTables() {
        addDrop(BlockTags.buttons());
        addDrop(BlockTags.plates());
        addDrop(COPPER_LANTERN);
        addDrop(FUZE_BLOCK);
        addDrop(WEATHERED_FUZE_BLOCK);
        addDrop(EXPOSED_FUZE_BLOCK);
        addDrop(OXIDIZED_FUZE_BLOCK);
    }

    private void addDrop(List<Block> blocks){
        blocks.forEach(this::addDrop);
    }
}
