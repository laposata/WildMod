package net.fabricmc.wildmod_copper.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;

public class ItemRegistry {

  public static final Item COPPER_NUGGET = register(new Item(new Item.Settings().group(ItemGroup.MISC).maxCount(64)), new Identifier(NAMESPACE, "copper_nugget"));
  public static void register(BlockItem item) {
    item.appendBlocks(Item.BLOCK_ITEMS, item);
    Registry.register(Registry.ITEM, Registry.BLOCK.getId(item.getBlock()), item);
  }

  public static Item register(Item item, Identifier id) {
    return Registry.register(Registry.ITEM, id, item);
  }

}
