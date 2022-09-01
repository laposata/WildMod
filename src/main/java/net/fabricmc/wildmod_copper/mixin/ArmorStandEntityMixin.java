package net.fabricmc.wildmod_copper.mixin;

import com.google.common.collect.ArrayListMultimap;
import net.fabricmc.wildmod_copper.imixin.ICountInventory;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(ArmorStandEntity.class)
public class ArmorStandEntityMixin implements ICountInventory {

  @Shadow @Final private DefaultedList<ItemStack> armorItems;

  @Shadow @Final private DefaultedList<ItemStack> heldItems;

  @Override
  public List<ItemStack> getTotalInventory() {
    ArrayList<ItemStack> items = new ArrayList<>();
    items.addAll(armorItems);
    items.addAll(heldItems);
    return items;
  }
}
