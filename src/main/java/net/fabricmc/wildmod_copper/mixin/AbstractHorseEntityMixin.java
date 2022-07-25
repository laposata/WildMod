package net.fabricmc.wildmod_copper.mixin;

import net.fabricmc.wildmod_copper.imixin.ICountInventory;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.SimpleInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractHorseEntity.class)
public class AbstractHorseEntityMixin implements ICountInventory {

    @Shadow
    protected SimpleInventory items;

    @Override
    public int getTotalInventory() {
        return ((ICountInventory)items).getTotalInventory();
    }
}
