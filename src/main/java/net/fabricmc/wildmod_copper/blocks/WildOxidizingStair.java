package net.fabricmc.wildmod_copper.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.OxidizableStairsBlock;

public class WildOxidizingStair extends OxidizableStairsBlock {
    public WildOxidizingStair(OxidationLevel oxidationLevel, BlockState baseBlockState, Settings settings) {
        super(oxidationLevel, baseBlockState, settings);
    }
}
