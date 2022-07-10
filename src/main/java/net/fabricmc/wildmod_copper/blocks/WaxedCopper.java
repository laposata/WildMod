package net.fabricmc.wildmod_copper.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OxidizableBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class WaxedCopper extends OxidizableBlock {
    public WaxedCopper(OxidationLevel oxidationLevel, Settings settings) {
        super(oxidationLevel, settings);
    }

    public static WaxedCopper wax(Block block){
        if(block instanceof OxidizableBlock){
            return new WaxedCopper(((OxidizableBlock)block).getDegradationLevel(), AbstractBlock.Settings.copy(block));
        }
        throw new IllegalArgumentException("Must pass in an Oxidizable block");
    }
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        return;
    }
}
