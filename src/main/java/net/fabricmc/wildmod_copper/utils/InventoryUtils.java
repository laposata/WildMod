package net.fabricmc.wildmod_copper.utils;

import net.minecraft.item.ItemStack;

public class InventoryUtils {
    public static int countBase64(ItemStack items){
        return items.getCount() * (64 / items.getMaxCount());
    }
}
