package net.fabricmc.wildmod_copper.data_providers.collections;


import net.fabricmc.wildmod_copper.utils.TagUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
  public static TagKey<Block> COPPERS = TagUtils.createBlockTag(new Identifier(NAMESPACE, "coppers"));
  public static TagKey<Block> EXPOSED_COPPERS = TagUtils.createBlockTag(new Identifier(NAMESPACE, "exposed_coppers"));
  public static TagKey<Block> WEATHERED_COPPERS = TagUtils.createBlockTag(new Identifier(NAMESPACE, "weathered_coppers"));
  public static TagKey<Block> OXIDIZED_COPPERS = TagUtils.createBlockTag(new Identifier(NAMESPACE, "oxidized_coppers"));

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

  public static List<Block> coppers(){
    return Arrays.asList(COPPER_BLOCK, WAXED_COPPER_BLOCK, CUT_COPPER, WAXED_CUT_COPPER);
  }


  public static List<Block> exposedCoppers(){
    return Arrays.asList(EXPOSED_COPPER, WAXED_EXPOSED_COPPER, EXPOSED_CUT_COPPER, WAXED_EXPOSED_CUT_COPPER);
  }

  public static List<Block> weatheredCoppers(){
    return Arrays.asList(WEATHERED_COPPER, WAXED_WEATHERED_COPPER, WEATHERED_CUT_COPPER, WAXED_WEATHERED_CUT_COPPER);
  }

  public static List<Block> oxidizedCoppers(){
    return Arrays.asList(OXIDIZED_COPPER, WAXED_OXIDIZED_COPPER, OXIDIZED_CUT_COPPER, WAXED_OXIDIZED_CUT_COPPER);
  }

  public static List<Block> shulkerBoxes(){
    return Arrays.asList(SHULKER_BOX, BLACK_SHULKER_BOX, GRAY_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX,
      RED_SHULKER_BOX, PINK_SHULKER_BOX, ORANGE_SHULKER_BOX, YELLOW_SHULKER_BOX,
      GREEN_SHULKER_BOX, LIME_SHULKER_BOX, BLUE_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX,
      CYAN_SHULKER_BOX, MAGENTA_SHULKER_BOX, PURPLE_SHULKER_BOX, BROWN_SHULKER_BOX, WHITE_SHULKER_BOX);
  }

}
