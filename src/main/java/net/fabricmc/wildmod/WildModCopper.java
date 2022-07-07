package net.fabricmc.wildmod;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.wildmod.registry.BlockRegistry.registerBlocks;

public class WildModCopper implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String NAMESPACE = "wildmod_copper";
	public static final String MINECRAFT_NAMESPACE = "minecraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

	@Override
	public void onInitialize() {
		registerBlocks();
		LOGGER.info("Hello Fabric world!");
	}

}
