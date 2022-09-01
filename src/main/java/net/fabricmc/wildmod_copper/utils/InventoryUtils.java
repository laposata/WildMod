package net.fabricmc.wildmod_copper.utils;

import net.fabricmc.wildmod_copper.imixin.ICountInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class InventoryUtils {
    public static int countBase64(ItemStack items){
        return items.getCount() * (64 / items.getMaxCount());
    }
    public static int getTotalItemCount(List<ItemStack> items){
        return items.stream()
          .flatMap(itemStack -> ICountInventory.burstInventoryContainingItems(itemStack, true).stream())
          .map(InventoryUtils::countBase64)
          .reduce(0, Integer::sum);
    }
}
