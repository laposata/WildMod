package net.fabricmc.wildmod_copper.mixin;

import net.fabricmc.wildmod_copper.utils.TagUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.fabricmc.wildmod_copper.registry.BlockTags.CHARGEABLE_COPPER;
import static net.fabricmc.wildmod_copper.registry.BlockTags.CONDUCTS_COPPER;
import static net.minecraft.block.Blocks.REDSTONE_WIRE;

@Mixin(RedstoneWireBlock.class)
public class RedstoneWireMixin {
	@Inject(at = @At("HEAD"), method = "connectsTo(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z", cancellable = true)
	private static void dontConnectToCopper(BlockState state, Direction dir, CallbackInfoReturnable<Boolean> cir) {
		if (TagUtils.blockIsIn(state.getBlock(), CHARGEABLE_COPPER)) {
			cir.setReturnValue(TagUtils.blockIsIn(REDSTONE_WIRE, CONDUCTS_COPPER));
		}
	}
}
