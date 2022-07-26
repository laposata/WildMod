package net.fabricmc.wildmod_copper.mixin;

import net.fabricmc.wildmod_copper.imixin.IPlaceBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(ItemPlacementContext.class)
public class ItemPlacementContextMixin extends ItemUsageContext implements IPlaceBlock {

    @Shadow
    public boolean canPlace() {return true;}

    public ItemPlacementContextMixin(PlayerEntity player, Hand hand, BlockHitResult hit) {
        super(player, hand, hit);
    }

    public Block blockBeingPlaced() {
        return canPlace() && getStack().getItem() instanceof BlockItem ?
                ((BlockItem)getStack().getItem()).getBlock(): null;
    }
}
