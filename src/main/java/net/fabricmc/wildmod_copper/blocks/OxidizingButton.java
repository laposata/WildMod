package net.fabricmc.wildmod_copper.blocks;


import net.devtech.arrp.json.recipe.JIngredient;
import net.devtech.arrp.json.recipe.JIngredients;
import net.fabricmc.wildmod_copper.blocks.states.ButtonState;
import net.fabricmc.wildmod_copper.imixin.IButtonsMixin;
import net.fabricmc.wildmod_copper.imixin.ICraftable;
import net.fabricmc.wildmod_copper.utils.resource.generators.JHelper;
import net.fabricmc.wildmod_copper.utils.resource.generators.SimpleBlockResourceGenerator;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;
import static net.fabricmc.wildmod_copper.utils.resource.generators.GenericResources.*;
import static net.minecraft.block.Oxidizable.OxidationLevel.UNAFFECTED;

public class OxidizingButton extends AbstractButtonBlock implements ICraftable {

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

  public void setRecipeAndTexture(){
    List<Item> cut = WildCopper.byLevel.get(oxidationLevel)
                       .stream()
                       .filter(b -> Registry.BLOCK.getId(b).toString().contains("cut"))
                       .map(Block::asItem).collect(Collectors.toList());

    shaplessCrafting(
      this.getIdentifier(),
      JIngredients.ingredients().add(
        JIngredient.ingredient()
          .tag(createIngredientTag(cut, oxidationLevel.name().toLowerCase() + "_cut_copper").toString())
      ),
      9);

    String texture = oxidationLevel.equals(Oxidizable.OxidationLevel.UNAFFECTED)?
                       "copper_block"
                       : oxidationLevel.toString().toLowerCase()+"_copper";

    addItemModel("minecraft:block/"+texture + "_inventory", getBlockName());
    addBlockModel("minecraft:block/button", "minecraft:block/"+texture, getBlockName());
    addBlockModel("minecraft:block/button_pressed", "minecraft:block/"+texture, getBlockName() + "_pressed");
    addBlockModel("minecraft:block/button_inventory", "minecraft:block/"+texture, getBlockName() + "_inventory");

    new SimpleBlockResourceGenerator(SERVER_PACK).generateSimpleBlockLootTable(getIdentifier());
    CLIENT_PACK.addResource(
      ResourceType.CLIENT_RESOURCES,
      new Identifier(NAMESPACE, "blockstates/" + getBlockName()+".json"),
      JHelper.serialize(ButtonState.state(
        new Identifier(NAMESPACE, getBlockName() + "_pressed").toString(),
        new Identifier(NAMESPACE, getBlockName()).toString()
      ))
    );
  }

  public Identifier getIdentifier(){
    return new Identifier(NAMESPACE, getBlockName());
  }
}
