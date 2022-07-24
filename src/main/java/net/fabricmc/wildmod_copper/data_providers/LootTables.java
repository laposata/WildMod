package net.fabricmc.wildmod_copper.data_providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.wildmod_copper.data_providers.collections.BlockTags;
import net.minecraft.block.Block;

import java.util.List;

import static net.fabricmc.wildmod_copper.registry.BlockRegistry.COPPER_BUTTON;
import static net.fabricmc.wildmod_copper.registry.BlockRegistry.OXIDIZED_COPPER_BUTTON;

public class LootTables extends FabricBlockLootTableProvider {

    public LootTables(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateBlockLootTables() {
        addDrop(BlockTags.buttons());
        addDrop(BlockTags.plates());
    }

    private void addDrop(List<Block> blocks){
        blocks.forEach(this::addDrop);
    }
}
