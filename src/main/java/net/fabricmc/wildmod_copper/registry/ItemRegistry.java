package net.fabricmc.wildmod_copper.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public class ItemRegistry {

  public static void register(BlockItem item) {
    item.appendBlocks(Item.BLOCK_ITEMS, item);
    Registry.register(Registry.ITEM, Registry.BLOCK.getId(item.getBlock()), item);
  }

}
