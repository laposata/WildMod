package net.fabricmc.wildmod.mixin;

import net.fabricmc.wildmod.blocks.WildCopper;
import net.fabricmc.wildmod.utils.TagUtils;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.fabricmc.wildmod.registry.Tags.CHARGEABLE_COPPER;
import static net.fabricmc.wildmod.registry.Tags.CONDUCTS_COPPER;
import static net.minecraft.block.HorizontalFacingBlock.FACING;

@Mixin(AbstractRedstoneGateBlock.class)
public class AbstractRedstoneGateMixin {

    @Inject(at=@At("RETURN"), method = "getPower", cancellable = true)
    public void getPowerFromCopper(World world, BlockPos pos, BlockState state,  CallbackInfoReturnable<Integer> cir){
        if(cir.getReturnValue() < 15 && TagUtils.blockIsIn(state.getBlock(), CONDUCTS_COPPER)){
            Direction direction = state.get(FACING);
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if(TagUtils.blockIsIn(block, CHARGEABLE_COPPER) && block instanceof WildCopper){
                cir.setReturnValue( TagUtils.blockIsIn(block, CHARGEABLE_COPPER) ?
                                          ((WildCopper)block).getRedstonePowerLevelForGates(blockState, state) :
                                          cir.getReturnValue());
            }
        }

    }

    @Inject(method = "getInputLevel", at=@At("RETURN"), cancellable = true)
    public void getInputLevelFromCopper(WorldView world, BlockPos pos, Direction dir, CallbackInfoReturnable<Integer> cir){
        int previous = cir.getReturnValue();
        if(previous < 15){
            BlockState blockState = world.getBlockState(pos);
            if (TagUtils.blockIsIn(blockState.getBlock(), CHARGEABLE_COPPER) && blockState.getBlock() instanceof WildCopper){
                BlockState original = world.getBlockState(pos.offset(dir.getOpposite()));
                if(TagUtils.blockIsIn (original.getBlock(), CONDUCTS_COPPER)) {
                    int copper = ((WildCopper)blockState.getBlock()).getRedstonePowerLevelForGates(blockState, original);
                    cir.setReturnValue(Math.max(copper, previous));
                }
            }
        }
    }
}
