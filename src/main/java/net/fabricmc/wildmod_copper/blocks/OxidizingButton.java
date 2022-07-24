package net.fabricmc.wildmod_copper.blocks;


import net.fabricmc.wildmod_copper.imixin.IButtonsMixin;
import net.minecraft.block.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.Locale;

import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;
import static net.minecraft.block.Oxidizable.OxidationLevel.UNAFFECTED;

public class OxidizingButton extends AbstractButtonBlock{

  private Oxidizable.OxidationLevel oxidationLevel;
  public OxidizingButton(Settings settings, Oxidizable.OxidationLevel level) {
    super(false, settings);
    ((IButtonsMixin)this).setGrade(level.ordinal() * 2 + 1);
    this.oxidationLevel = level;
  }

  @Override
  protected SoundEvent getClickSound(boolean powered) {
    return powered ? SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;
  }

  public static void balanceOtherButtonTypes(){
    ((IButtonsMixin)Blocks.POLISHED_BLACKSTONE_BUTTON).setGrade(2);
    ((IButtonsMixin)Blocks.STONE_BUTTON).setGrade(4);
  }

  public String getBlockName(){
    return (oxidationLevel == UNAFFECTED ? "" : oxidationLevel.toString().toLowerCase(Locale.ROOT) + "_" ) + "copper_button";
  }
  public Identifier getIdentifier(){
    return new Identifier(NAMESPACE, getBlockName());
  }
}
