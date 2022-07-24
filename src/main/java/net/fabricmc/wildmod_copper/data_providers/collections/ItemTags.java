package net.fabricmc.wildmod_copper.data_providers.collections;

import net.fabricmc.wildmod_copper.utils.TagUtils;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;

public class ItemTags {
    public static TagKey<Item> COPPERS_ITEM = TagUtils.createItemTag(new Identifier(NAMESPACE, "coppers_item"));
    public static TagKey<Item> EXPOSED_COPPERS_ITEM = TagUtils.createItemTag(new Identifier(NAMESPACE, "exposed_coppers_item"));
    public static TagKey<Item> WEATHERED_COPPERS_ITEM = TagUtils.createItemTag(new Identifier(NAMESPACE, "weathered_coppers_item"));
    public static TagKey<Item> OXIDIZED_COPPERS_ITEM = TagUtils.createItemTag(new Identifier(NAMESPACE, "oxidized_coppers_item"));
}
