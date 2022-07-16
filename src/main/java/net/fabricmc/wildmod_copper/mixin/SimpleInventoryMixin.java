package net.fabricmc.wildmod_copper.mixin;

import net.fabricmc.wildmod_copper.imixin.ICountInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SimpleInventory.class)
public class SimpleInventoryMixin implements ICountInventory {

  @Shadow @Final
  private DefaultedList<ItemStack> stacks;

  public int getTotalInventory(){
    return stacks.stream()
             .map(stack -> stack.getCount() * (64 / stack.getMaxCount()))
             .reduce(0, Integer::sum);
  }


}
