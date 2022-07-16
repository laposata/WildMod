package net.fabricmc.wildmod_copper.utils;

import net.devtech.arrp.json.tags.JTag;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.wildmod_copper.utils.resource.generators.GenericResources.SERVER_PACK;

public class TagUtils {
    public static boolean blockIsIn(Block block, TagKey<Block> key){
        return Registry.BLOCK.getOrCreateEntry(Registry.BLOCK.getKey(block).get()).isIn(key);
    }
    public static TagKey<Block> createTag(Identifier tag){
        return TagKey.of(Registry.BLOCK_KEY,tag);
    }

    public static JTag tag(Iterable<Identifier> items, Identifier name){
        JTag tag = JTag.tag();
        for(Identifier i: items) {
            tag.add(i);
        }
        createTag(name);
        SERVER_PACK.addTag(name, tag);
        return tag;
    }
}
