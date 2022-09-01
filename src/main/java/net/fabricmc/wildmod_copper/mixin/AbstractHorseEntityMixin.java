package net.fabricmc.wildmod_copper.mixin;

import net.fabricmc.wildmod_copper.imixin.ICountInventory;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(AbstractHorseEntity.class)
public class AbstractHorseEntityMixin implements ICountInventory {

    @Shadow
    protected SimpleInventory items;

    @Override
    public List<ItemStack> getTotalInventory() {
        return ((ICountInventory)items).getTotalInventory();
    }
}
