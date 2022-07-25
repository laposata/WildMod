package net.fabricmc.wildmod_copper.mixin;

import net.fabricmc.wildmod_copper.imixin.ICountInventory;
import net.fabricmc.wildmod_copper.utils.InventoryUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin implements ICountInventory {

  @Shadow @Final
  private List<DefaultedList<ItemStack>> combinedInventory;

  @Override
  public int getTotalInventory() {
    return combinedInventory.stream()
             .map(subInventory ->
                subInventory.stream()
                  .map(InventoryUtils::countBase64)
                  .reduce(0, Integer::sum)
              ).reduce(0, Integer::sum);
  }
}
