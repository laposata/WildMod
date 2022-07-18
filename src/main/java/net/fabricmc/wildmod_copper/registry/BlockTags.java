package net.fabricmc.wildmod_copper.registry;


import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.wildmod_copper.utils.TagUtils;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;

import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;
import static net.fabricmc.wildmod_copper.registry.BlockRegistry.*;
import static net.minecraft.block.Blocks.*;


public class BlockTags {

  private static final String chargeable_copper = "chargeable_copper";
  private static final String conducts_copper = "conducts_copper";
  private static final Identifier chargeable_copper_id = new Identifier(NAMESPACE, chargeable_copper);
  private static final Identifier conducts_copper_id = new Identifier(NAMESPACE, conducts_copper);
  public static TagKey<Block> CHARGEABLE_COPPER = TagUtils.createBlockTag(chargeable_copper_id);
  public static TagKey<Block> CONDUCTS_COPPER = TagUtils.createBlockTag(conducts_copper_id);
  public static List<Block> chargeableCopper() {
    return Arrays.asList(
            COPPER_BLOCK,
            EXPOSED_COPPER,
            WEATHERED_COPPER,
            OXIDIZED_COPPER,
            WAXED_COPPER_BLOCK,
            WAXED_EXPOSED_COPPER,
            WAXED_WEATHERED_COPPER,
            WAXED_OXIDIZED_COPPER);
  }

  public static List<Block> conductsCopper() {
    return Arrays.asList(
        REPEATER,
        COMPARATOR,
        REDSTONE_WALL_TORCH,
        REDSTONE_TORCH,
        REDSTONE_LAMP,
        IRON_DOOR,
        IRON_TRAPDOOR,
        HOPPER,
        NOTE_BLOCK,
        DISPENSER,
        DROPPER,
        DRAGON_HEAD,
        BELL,
        PISTON,
        STICKY_PISTON,
        ACTIVATOR_RAIL,
        POWERED_RAIL,
        TNT);
  }

  public static List<Block> buttons(){
    return Arrays.asList(EXPOSED_COPPER_BUTTON, COPPER_BUTTON, WEATHERED_COPPER_BUTTON, OXIDIZED_COPPER_BUTTON);
  }

  public static List<Block> plates(){
    return Arrays.asList(COPPER_PRESSURE_PLATE, EXPOSED_COPPER_PRESSURE_PLATE, WEATHERED_COPPER_PRESSURE_PLATE, OXIDIZED_COPPER_PRESSURE_PLATE);
  }


}
