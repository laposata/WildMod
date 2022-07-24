package net.fabricmc.wildmod_copper.data_providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

import static net.fabricmc.wildmod_copper.registry.BlockRegistry.*;
import static net.minecraft.block.Blocks.*;

public class Models extends FabricModelProvider {
    public Models(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerCubeAllModelTexturePool(COPPER_BLOCK).button(COPPER_BUTTON);
        blockStateModelGenerator.registerCubeAllModelTexturePool(WEATHERED_COPPER).button(WEATHERED_COPPER_BUTTON);
        blockStateModelGenerator.registerCubeAllModelTexturePool(EXPOSED_COPPER).button(EXPOSED_COPPER_BUTTON);
        blockStateModelGenerator.registerCubeAllModelTexturePool(OXIDIZED_COPPER).button(OXIDIZED_COPPER_BUTTON);
        blockStateModelGenerator.registerPressurePlate(COPPER_PRESSURE_PLATE, COPPER_BLOCK);
        blockStateModelGenerator.registerPressurePlate(WEATHERED_COPPER_PRESSURE_PLATE, WEATHERED_COPPER);
        blockStateModelGenerator.registerPressurePlate(EXPOSED_COPPER_PRESSURE_PLATE, EXPOSED_COPPER);
        blockStateModelGenerator.registerPressurePlate(OXIDIZED_COPPER_PRESSURE_PLATE, OXIDIZED_COPPER);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
