package net.fabricmc.wildmod_copper.imixin;

import net.fabricmc.wildmod_copper.data_providers.collections.ItemTags;
import net.fabricmc.wildmod_copper.utils.InventoryUtils;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.block.entity.ShulkerBoxBlockEntity.ITEMS_KEY;

public interface ICountInventory {
  static final String BLOCK_ENTITY_TAG_KEY = "BlockEntityTag";

  List<ItemStack>  getTotalInventory();

  default int getTotalInventoryCount(){
    return InventoryUtils.getTotalItemCount(getTotalInventory());
  }

  static boolean isContainer(ItemStack stack){
    return stack.isIn(ItemTags.INVENTORY_ITEMS) && stack.getNbt() != null;
  }

  static List<ItemStack> burstInventoryContainingItems(ItemStack item, boolean countContainer){
    List<ItemStack> output = new ArrayList<>();
    if(isContainer(item)){
      List<ItemStack> inventory = readInventoryNbt(item.getNbt());
      for(ItemStack subItem: inventory){
        if(isContainer(subItem)){
          output.addAll(burstInventoryContainingItems(subItem, countContainer));
        } else{
          output.add(subItem);
        }
      }
      if(countContainer){
        ItemStack dummy = Items.AIR.getDefaultStack();
        dummy.setCount(64);
        output.add(dummy);
      }
    } else {
      output.add(item);
    }
    return output ;
  }

  private static List<ItemStack> readInventoryNbt(NbtCompound nbt) {
    DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
    if(nbt.contains(BLOCK_ENTITY_TAG_KEY)) {
      nbt = nbt.getCompound(BLOCK_ENTITY_TAG_KEY);
      if (nbt.contains(ITEMS_KEY, NbtElement.LIST_TYPE)) {
        Inventories.readNbt(nbt, inventory);
        return inventory;
      }
    }
    return new ArrayList<>();
  }
}
