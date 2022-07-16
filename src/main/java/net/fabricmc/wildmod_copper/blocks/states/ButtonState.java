package net.fabricmc.wildmod_copper.blocks.states;

import net.fabricmc.wildmod_copper.utils.resource.generators.ToxBlockState;
import net.minecraft.block.Blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ButtonState {
  public static String FACE = "face";
  public static String FACING = "facing";
  public static String POWERED = "powered";
  public static ToxBlockState state(String poweredModel, String offModel){
    return new ToxBlockState()
        .addState(POWERED, "true", "false")
        .addState(FACING, "east", "north", "south", "west")
        .addState(FACE, "ceiling", "floor", "wall")
        .set(new ToxBlockState.State(Map.of(FACE, "wall")),"uvlock", "true")
        .set(new ToxBlockState.State(Map.of(FACE, "wall")),"x", 90)
        .set(new ToxBlockState.State(Map.of(POWERED, "true")),"model", poweredModel)
        .set(new ToxBlockState.State(Map.of(POWERED, "false")),"model", offModel)
        .set(new ToxBlockState.State(Map.of(FACE, "ceiling")),"x", 180)
        .set(new ToxBlockState.State(Map.of(FACE, "south")),"y", 180)
        .set(new ToxBlockState.State(Map.of(FACE, "ceiling", FACING, "east")),"y", 270)
        .set(new ToxBlockState.State(Map.of(FACE, "ceiling", FACING, "north")),"y", 180)
        .set(new ToxBlockState.State(Map.of(FACE, "ceiling", FACING, "west")),"y", 90)
        .set(new ToxBlockState.State(Map.of(FACE, "wall", FACING, "east")),"y", 90)
        .set(new ToxBlockState.State(Map.of(FACE, "wall", FACING, "west")),"y", 270);
  }

}
