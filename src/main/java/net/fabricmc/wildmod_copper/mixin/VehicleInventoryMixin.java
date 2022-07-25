package net.fabricmc.wildmod_copper.mixin;

import net.fabricmc.wildmod_copper.imixin.ICountInventory;
import net.fabricmc.wildmod_copper.utils.InventoryUtils;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.fabricmc.wildmod_copper.utils.InventoryUtils.countBase64;

@Mixin(VehicleInventory.class)
public interface VehicleInventoryMixin extends ICountInventory {

    @Shadow
    DefaultedList<ItemStack> getInventory();

    @Override
    default int getTotalInventory() {
        return getInventory().stream().map(InventoryUtils::countBase64).reduce(0, Integer::sum);
    }
}
