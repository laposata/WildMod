package net.fabricmc.wildmod_copper.data_providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.wildmod_copper.registry.BlockTags;
import net.fabricmc.wildmod_copper.utils.TagUtils;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;
import static net.fabricmc.wildmod_copper.registry.BlockTags.CHARGEABLE_COPPER;
import static net.fabricmc.wildmod_copper.registry.BlockTags.CONDUCTS_COPPER;
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
            getOrCreateTagBuilder(BUTTONS).add(BlockTags.conductsCopper().toArray(Block[]::new));
            getOrCreateTagBuilder(PRESSURE_PLATES).add(BlockTags.conductsCopper().toArray(Block[]::new));
        }
    }
    public static class Items extends FabricTagProvider.ItemTagProvider{

        public Items(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateTags() {

        }
    }

}
