package net.fabricmc.wildmod_copper.mixin;

import net.fabricmc.wildmod_copper.blocks.WaxedCopper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import static net.fabricmc.wildmod_copper.WildModCopper.LOGGER;
import static org.spongepowered.asm.mixin.injection.At.Shift.BEFORE;

@Mixin(Blocks.class)
public abstract class BlocksMixin {

  @Redirect(
    slice = @Slice(
      from = @At(
        value = "CONSTANT",
        args= {
          "stringValue=waxed_copper_block"
        },
        ordinal = 0
      )
    ),
    at = @At(
      value = "NEW",
      target = "Lnet/minecraft/block/Block;*",
      ordinal = 0
    ),
    method = "<clinit>")
  private static Block copper(AbstractBlock.Settings settings) {
    LOGGER.info(settings.toString());
    return WaxedCopper.WILD_WAXED_COPPER(settings);
  }

  @Redirect(
    slice = @Slice(
      from = @At(
        value = "CONSTANT",
        args= {
          "stringValue=waxed_exposed_copper"
        },
        ordinal = 0
      )
    ),

    at = @At(
      value = "NEW",
      target = "Lnet/minecraft/block/Block;*"
    ),
    method = "<clinit>")
  private static Block exposed(AbstractBlock.Settings settings) {
    return WaxedCopper.WILD_WAXED_COPPER_E(settings);
  }

  @Redirect(
    slice = @Slice(
      from = @At(
        value = "CONSTANT",
        args= {
          "stringValue=waxed_weathered_copper"
        },
        ordinal = 0
      )
    ),
    at = @At(
      value = "NEW",
      target = "Lnet/minecraft/block/Block;*"
    ),
    method = "<clinit>")
  private static Block weathered(AbstractBlock.Settings settings) {
    return WaxedCopper.WILD_WAXED_COPPER_W(settings);
  }

  @Redirect(
    slice = @Slice(
      from = @At(
        value = "CONSTANT",
        args= {
          "stringValue=waxed_oxidized_copper"
        },
        ordinal = 0
      )
    ),
    at = @At(
      value = "NEW",
      target = "Lnet/minecraft/block/Block;*"
    ),
    method = "<clinit>")
  private static Block oxidized(AbstractBlock.Settings settings) {
    return WaxedCopper.WILD_WAXED_COPPER_O(settings);
  }

  @Redirect(
    slice = @Slice(
      from = @At(
        value = "CONSTANT",
        args= {
          "stringValue=waxed_cut_copper"
        },
        ordinal = 0
      )
    ),
    at = @At(
      value = "NEW",
      target = "Lnet/minecraft/block/Block;*",
      ordinal = 0
    ),
    method = "<clinit>")
  private static Block copperCut(AbstractBlock.Settings settings) {
    LOGGER.info(settings.toString());
    return WaxedCopper.WILD_CUT_WAXED_COPPER(settings);
  }

  @Redirect(
    slice = @Slice(
      from = @At(
        value = "CONSTANT",
        args= {
          "stringValue=waxed_exposed_cut_copper"
        },
        ordinal = 0
      )
    ),

    at = @At(
      value = "NEW",
      target = "Lnet/minecraft/block/Block;*"
    ),
    method = "<clinit>")
  private static Block exposedCut(AbstractBlock.Settings settings) {
    return WaxedCopper.WILD_CUT_WAXED_COPPER_E(settings);
  }

  @Redirect(
    slice = @Slice(
      from = @At(
        value = "CONSTANT",
        args= {
          "stringValue=waxed_weathered_cut_copper"
        },
        ordinal = 0
      )
    ),
    at = @At(
      value = "NEW",
      target = "Lnet/minecraft/block/Block;*"
    ),
    method = "<clinit>")
  private static Block weatheredCut(AbstractBlock.Settings settings) {
    return WaxedCopper.WILD_CUT_WAXED_COPPER_W(settings);
  }

  @Redirect(
    slice = @Slice(
      from = @At(
        value = "CONSTANT",
        args= {
          "stringValue=waxed_oxidized_cut_copper"
        },
        ordinal = 0
      )
    ),
    at = @At(
      value = "NEW",
      target = "Lnet/minecraft/block/Block;*"
    ),
    method = "<clinit>")
  private static Block oxidizedCut(AbstractBlock.Settings settings) {
    return WaxedCopper.WILD_CUT_WAXED_COPPER_O(settings);
  }


}
