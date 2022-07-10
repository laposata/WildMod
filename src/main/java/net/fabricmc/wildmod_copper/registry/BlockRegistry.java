package net.fabricmc.wildmod_copper.registry;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;

import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;
import static net.fabricmc.wildmod_copper.registry.Tags.generateTagData;

public class BlockRegistry {
  public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(NAMESPACE + ":" + "pack");
  public static final RuntimeResourcePack CLIENT_PACK = RuntimeResourcePack.create(NAMESPACE + ":" + "datapack");

  public static void registerBlocks() {
    generateTagData(CLIENT_PACK);
    RRPCallback.BEFORE_VANILLA.register(a -> a.add(RESOURCE_PACK));
    RRPCallback.BEFORE_VANILLA.register(a -> a.add(CLIENT_PACK));
  }
}
