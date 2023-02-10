package net.fabricmc.wildmod_copper.mixin;

import net.fabricmc.wildmod_copper.blocks.AbstractFuseBlock;
import net.fabricmc.wildmod_copper.utils.PropertyUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {

    @Redirect(
        method = "getEmittedRedstonePower",
        at = @At(value="INVOKE",
                target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z")
    )
    public boolean dontLetBurnoutOutput(BlockState instance, BlockView blockView, BlockPos blockPos) {
        if(instance.getBlock() instanceof AbstractFuseBlock) return false;
        return instance.isSolidBlock(blockView, blockPos);
    }

}
