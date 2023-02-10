package net.fabricmc.wildmod_copper.registry;

import net.fabricmc.wildmod_copper.blocks.AbstractFuseBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.state.property.IntProperty;

import static net.fabricmc.wildmod_copper.blocks.AbstractFuseBlock.BURNT_OUT;

public class BlockFactories {

    public static AbstractFuseBlock makeFuse(Oxidizable.OxidationLevel level, AbstractBlock.Settings settings){
        AbstractBlock.ContextPredicate isSolid = (state, world, pos) ->
                !(state.getBlock() instanceof AbstractFuseBlock) || !state.get(BURNT_OUT);
        settings = settings.ticksRandomly().solidBlock(isSolid);
        AbstractFuseBlock fuse = new AbstractFuseBlock(settings, level) {
            @Override
            public IntProperty POWER() {
                return POWER_LEVELS.get(level);
            }
        };

        return fuse;
    }
}
