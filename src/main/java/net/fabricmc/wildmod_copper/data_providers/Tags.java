package net.fabricmc.wildmod_copper.data_providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.wildmod_copper.data_providers.collections.BlockTags;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import static net.fabricmc.wildmod_copper.data_providers.collections.BlockTags.*;
import static net.fabricmc.wildmod_copper.data_providers.collections.ItemTags.*;
import static net.fabricmc.wildmod_copper.utils.TagUtils.itemsFromBlocks;
import static net.minecraft.tag.BlockTags.BUTTONS;
import static net.minecraft.tag.BlockTags.PRESSURE_PLATES;

public class Tags {
    public static class Blocks extends FabricTagProvider.BlockTagProvider{

        public Blocks(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateTags() {
            getOrCreateTagBuilder(CHARGEABLE_COPPER).add(BlockTags.chargeableCopper().toArray(Block[]::new));
            getOrCreateTagBuilder(CONDUCTS_COPPER).add(BlockTags.conductsCopper().toArray(Block[]::new));
            getOrCreateTagBuilder(BUTTONS).add(BlockTags.buttons().toArray(Block[]::new));
            getOrCreateTagBuilder(PRESSURE_PLATES).add(BlockTags.plates().toArray(Block[]::new));
            getOrCreateTagBuilder(COPPERS).add(BlockTags.coppers().toArray(Block[]::new));
            getOrCreateTagBuilder(EXPOSED_COPPERS).add(BlockTags.exposedCoppers().toArray(Block[]::new));
            getOrCreateTagBuilder(WEATHERED_COPPERS).add(BlockTags.weatheredCoppers().toArray(Block[]::new));
            getOrCreateTagBuilder(OXIDIZED_COPPERS).add(BlockTags.oxidizedCoppers().toArray(Block[]::new));
        }
    }
    public static class Items extends FabricTagProvider.ItemTagProvider{

        public Items(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateTags() {
            getOrCreateTagBuilder(COPPERS_ITEM).add(itemsFromBlocks(BlockTags.coppers()).toArray(Item[]::new));
            getOrCreateTagBuilder(EXPOSED_COPPERS_ITEM).add(itemsFromBlocks(BlockTags.exposedCoppers()).toArray(Item[]::new));
            getOrCreateTagBuilder(WEATHERED_COPPERS_ITEM).add(itemsFromBlocks(BlockTags.weatheredCoppers()).toArray(Item[]::new));
            getOrCreateTagBuilder(OXIDIZED_COPPERS_ITEM).add(itemsFromBlocks(BlockTags.oxidizedCoppers()).toArray(Item[]::new));
        }
    }

}
