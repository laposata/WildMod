package net.fabricmc.wildmod_copper.blocks;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.HashMap;
import java.util.LinkedHashMap;
import static net.fabricmc.wildmod_copper.WildModCopper.LOGGER;
public class WaxedCopper extends OxidizableBlock {
    public WaxedCopper(OxidationLevel oxidationLevel, Settings settings) {
        super(oxidationLevel, settings);
    }

    public static WaxedCopper wax(OxidationLevel level, AbstractBlock.Settings settings){
        return new WaxedCopper(level, settings);
    }
    public static Block WILD_WAXED_COPPER(AbstractBlock.Settings settings){
        return wax(OxidationLevel.UNAFFECTED, settings);
    }
    public static Block WILD_WAXED_COPPER_E(AbstractBlock.Settings settings){return wax(OxidationLevel.EXPOSED, settings);}
    public static Block WILD_WAXED_COPPER_W(AbstractBlock.Settings settings){return wax(OxidationLevel.WEATHERED, settings);}
    public static Block WILD_WAXED_COPPER_O(AbstractBlock.Settings settings){return wax(OxidationLevel.OXIDIZED, settings);}
    public static Block WILD_CUT_WAXED_COPPER(AbstractBlock.Settings settings){return wax(OxidationLevel.UNAFFECTED, settings);}
    public static Block WILD_CUT_WAXED_COPPER_E(AbstractBlock.Settings settings){return wax(OxidationLevel.EXPOSED, settings);}
    public static Block WILD_CUT_WAXED_COPPER_W(AbstractBlock.Settings settings){return wax(OxidationLevel.WEATHERED, settings);}
    public static Block WILD_CUT_WAXED_COPPER_O(AbstractBlock.Settings settings){return wax(OxidationLevel.OXIDIZED, settings);}
}
