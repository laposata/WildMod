package net.fabricmc.wildmod_copper.data_providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.fabricmc.wildmod_copper.data_providers.collections.BlockTags;
import net.fabricmc.wildmod_copper.utils.TagUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;
import static net.fabricmc.wildmod_copper.data_providers.collections.BlockTags.*;
import static net.fabricmc.wildmod_copper.data_providers.collections.ItemTags.*;
import static net.fabricmc.wildmod_copper.utils.TagUtils.itemsFromBlocks;
import static net.minecraft.tag.BlockTags.*;

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
            getOrCreateTagBuilder(NEEDS_IRON_TOOL).add(BlockTags.newBlocks().toArray(Block[]::new));
            getOrCreateTagBuilder(PICKAXE_MINEABLE).add(BlockTags.newBlocks().toArray(Block[]::new));
            getOrCreateTagBuilder(FUZES).add(BlockTags.fuses().toArray(Block[]::new));
        }
    }
    public static class Items extends FabricTagProvider.ItemTagProvider{

        public Items(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateTags() {
            getOrCreateTagBuilder(TagUtils.createItemTag(new Identifier("c", "shulker_boxes")))
              .add(itemsFromBlocks(BlockTags.shulkerBoxes()).toArray(Item[]::new));
            getOrCreateTagBuilder(COPPERS_ITEM).add(itemsFromBlocks(BlockTags.coppers()).toArray(Item[]::new));
            getOrCreateTagBuilder(EXPOSED_COPPERS_ITEM).add(itemsFromBlocks(BlockTags.exposedCoppers()).toArray(Item[]::new));
            getOrCreateTagBuilder(WEATHERED_COPPERS_ITEM).add(itemsFromBlocks(BlockTags.weatheredCoppers()).toArray(Item[]::new));
            getOrCreateTagBuilder(OXIDIZED_COPPERS_ITEM).add(itemsFromBlocks(BlockTags.oxidizedCoppers()).toArray(Item[]::new));
            getOrCreateTagBuilder(INVENTORY_ITEMS).addTag(ConventionalItemTags.SHULKER_BOXES).add(net.minecraft.item.Items.BUNDLE);
        }
    }

}
