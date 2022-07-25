package net.fabricmc.wildmod_copper.mixin;

import net.fabricmc.wildmod_copper.imixin.ICountInventory;
import net.fabricmc.wildmod_copper.utils.InventoryUtils;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.fabricmc.wildmod_copper.utils.InventoryUtils.countBase64;

@Mixin(SimpleInventory.class)
public class SimpleInventoryMixin implements ICountInventory {

  @Shadow @Final
  private DefaultedList<ItemStack> stacks;

  public int getTotalInventory(){
    return stacks.stream()
             .map(InventoryUtils::countBase64)
             .reduce(0, Integer::sum);
  }


}
