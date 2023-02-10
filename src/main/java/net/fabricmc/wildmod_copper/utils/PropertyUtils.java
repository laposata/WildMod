package net.fabricmc.wildmod_copper.utils;

import net.fabricmc.wildmod_copper.blocks.AbstractFuseBlock;
import net.fabricmc.wildmod_copper.blocks.WildCopper;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;

import static net.fabricmc.wildmod_copper.blocks.AbstractFuseBlock.BURNT_OUT;

public class PropertyUtils {
    public static <T extends Comparable<T>> T propertyWithDefault(BlockState state, Property<T> property, T defaultVal){
        return state.getProperties().contains(property)?
                     state.get(property)
                     : defaultVal;
    }

    public static boolean waterlogged(BlockState state){
        return propertyWithDefault(state, Properties.WATERLOGGED, false);
    }

    public static int charged(BlockState state){
        return propertyWithDefault(state, WildCopper.CHARGE, 0);
    }
    public static boolean burntOut(BlockState state){
        return propertyWithDefault(state, BURNT_OUT, false);
    }

}
