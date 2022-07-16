package net.fabricmc.wildmod_copper.blocks.states;

import net.fabricmc.wildmod_copper.utils.resource.generators.ToxBlockState;

import java.util.Collections;
import java.util.Map;

public class PlateState {
  public static String POWERED = "power";
  public static ToxBlockState state(String poweredModel, String offModel) {
    return new ToxBlockState()
             .addState(POWERED, 0,15)
               .setUnless(new ToxBlockState.State(Map.of(POWERED, "0")), "model", poweredModel)
               .set(new ToxBlockState.State(Map.of(POWERED, "0")), "model", offModel);
  }
}
