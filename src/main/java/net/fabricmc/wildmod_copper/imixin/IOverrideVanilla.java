package net.fabricmc.wildmod_copper.imixin;

import net.minecraft.block.Block;
import net.minecraft.util.registry.RegistryEntry;

public interface IOverrideVanilla {
     void setRegistryEntry(RegistryEntry.Reference<Block> registryEntry);
}
