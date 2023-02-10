package net.fabricmc.wildmod_copper.blocks;

import net.minecraft.block.Oxidizable;
import net.minecraft.block.WallBlock;

public class WildOxidizingWall extends WallBlock implements Oxidizable {
    public WildOxidizingWall(Settings settings) {
        super(settings);
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return null;
    }
}
