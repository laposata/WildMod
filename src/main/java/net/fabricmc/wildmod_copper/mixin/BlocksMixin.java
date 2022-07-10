package net.fabricmc.wildmod_copper.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.OxidizableBlock;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.fabricmc.wildmod_copper.WildModCopper.LOGGER;
import static net.minecraft.block.Blocks.*;

@Mixin(Blocks.class)
public class BlocksMixin {
    @Mutable
    @Shadow
    @Final
    private static Block WAXED_COPPER_BLOCK,
          WAXED_EXPOSED_COPPER,
          WAXED_WEATHERED_COPPER,
          WAXED_OXIDIZED_COPPER;

    @Shadow
    private static Block register(String id, Block block) {
        return null;
    }

    private static Block newWaxedCopper  = new OxidizableBlock(((OxidizableBlock) COPPER_BLOCK).getDegradationLevel(), AbstractBlock.Settings.copy(COPPER_BLOCK));
    private static Block newWaxedCopperE = new OxidizableBlock(((OxidizableBlock) EXPOSED_COPPER).getDegradationLevel(), AbstractBlock.Settings.copy(EXPOSED_COPPER));
    private static Block newWaxedCopperW = new OxidizableBlock(((OxidizableBlock) WEATHERED_COPPER).getDegradationLevel(), AbstractBlock.Settings.copy(WEATHERED_COPPER));
    private static Block newWaxedCopperO = new OxidizableBlock(((OxidizableBlock) OXIDIZED_COPPER).getDegradationLevel(), AbstractBlock.Settings.copy(OXIDIZED_COPPER));

    @Redirect(method = "<clinit>",
          at = @At(
                value="FIELD",
                opcode = Opcodes.PUTSTATIC,
                target="Lnet/minecraft/block/Blocks;WAXED_COPPER_BLOCK:Lnet/minecraft/block/Block;"
          ))
    private static void copper(Block value) {
        LOGGER.info("ca");
        WAXED_COPPER_BLOCK = register("waxed_copper_block",newWaxedCopper);
        LOGGER.info("cb");
    }

    @Redirect(method = "<clinit>",
          at = @At(
                value="FIELD",
                opcode = Opcodes.PUTSTATIC,
                target="Lnet/minecraft/block/Blocks;WAXED_EXPOSED_COPPER:Lnet/minecraft/block/Block;"
          ))
    private static void exposedCopper(Block value) {
        LOGGER.info("ea");
        WAXED_EXPOSED_COPPER = register("waxed_exposed_copper", newWaxedCopperE);
        LOGGER.info("eb");
    }

    @Redirect(method = "<clinit>",
          at = @At(
                value="FIELD",
                opcode = Opcodes.PUTSTATIC,
                target="Lnet/minecraft/block/Blocks;WAXED_WEATHERED_COPPER:Lnet/minecraft/block/Block;"
          ))
    private static void weatheredCopper(Block value) {
        LOGGER.info("wa");
        WAXED_WEATHERED_COPPER = register("waxed_weathered_copper", newWaxedCopperW);
        LOGGER.info("wb");
    }

    @Redirect(method = "<clinit>",
          at = @At(
                value="FIELD",
                opcode = Opcodes.PUTSTATIC,
                target="Lnet/minecraft/block/Blocks;WAXED_OXIDIZED_COPPER:Lnet/minecraft/block/Block;"
          ))
    private static void oxidizedCopper(Block value) {
        LOGGER.info("oa");
        WAXED_OXIDIZED_COPPER = register("waxed_oxidized_copper", newWaxedCopperO);
        LOGGER.info("ob");
    }
}
