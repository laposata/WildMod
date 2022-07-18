package net.fabricmc.wildmod_copper.utils;

import net.minecraft.block.Block;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class TagUtils {
    public static boolean blockIsIn(Block block, TagKey<Block> key){
        return Registry.BLOCK.getOrCreateEntry(Registry.BLOCK.getKey(block).get()).isIn(key);
    }
    public static TagKey<Block> createBlockTag(Identifier tag){
        return TagKey.of(Registry.BLOCK_KEY,tag);
    }
    public static TagKey<Item> createItemTag(Identifier tag){
        return TagKey.of(Registry.ITEM_KEY,tag);
    }


}
