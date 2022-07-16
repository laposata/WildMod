package net.fabricmc.wildmod_copper.registry;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.tags.JTag;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagBuilder;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.wildmod_copper.WildModCopper.MINECRAFT_NAMESPACE;
import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;
import static net.fabricmc.wildmod_copper.registry.BlockRegistry.*;
import static net.fabricmc.wildmod_copper.utils.TagUtils.createTag;
import static net.minecraft.block.Blocks.*;


public class Tags extends AbstractTagProvider<Block> {
  public Tags(DataGenerator root) {
    super(root, Registry.BLOCK);
  }

  private static final String chargeable_copper = "chargeable_copper";
  private static final String conducts_copper = "conducts_copper";
  public static TagKey<Block> CHARGEABLE_COPPER = createTag(new Identifier(NAMESPACE, chargeable_copper));
  public static TagKey<Block> CONDUCTS_COPPER = createTag(new Identifier(NAMESPACE, conducts_copper));
  @Override
  protected void configure() {
    addButtons();
    addPlates();
  }
  public static void generateTagData(RuntimeResourcePack pack) {
    setChargeableCopper(pack);
    setConductsCopper(pack);

  }

  private static void setChargeableCopper(RuntimeResourcePack pack) {
    pack.addTag(new Identifier(NAMESPACE, "blocks/" + chargeable_copper),
      new JTag()
        .add(Registry.BLOCK.getId(COPPER_BLOCK))
        .add(Registry.BLOCK.getId(EXPOSED_COPPER))
        .add(Registry.BLOCK.getId(WEATHERED_COPPER))
        .add(Registry.BLOCK.getId(OXIDIZED_COPPER))
        .add(Registry.BLOCK.getId(WAXED_COPPER_BLOCK))
        .add(Registry.BLOCK.getId(WAXED_EXPOSED_COPPER))
        .add(Registry.BLOCK.getId(WAXED_WEATHERED_COPPER))
        .add(Registry.BLOCK.getId(WAXED_OXIDIZED_COPPER)));
  }

  private static void setConductsCopper(RuntimeResourcePack pack) {
    pack.addTag(new Identifier(NAMESPACE, "blocks/" + conducts_copper),
      new JTag()
        .add(Registry.BLOCK.getId(REPEATER))
        .add(Registry.BLOCK.getId(COMPARATOR))
        .add(Registry.BLOCK.getId(REDSTONE_WALL_TORCH))
        .add(Registry.BLOCK.getId(REDSTONE_TORCH))
        .add(Registry.BLOCK.getId(REDSTONE_LAMP))
        .add(Registry.BLOCK.getId(IRON_DOOR))
        .add(Registry.BLOCK.getId(IRON_TRAPDOOR))
        .add(Registry.BLOCK.getId(HOPPER))
        .add(Registry.BLOCK.getId(NOTE_BLOCK))
        .add(Registry.BLOCK.getId(DISPENSER))
        .add(Registry.BLOCK.getId(DROPPER))
        .add(Registry.BLOCK.getId(DRAGON_HEAD))
        .add(Registry.BLOCK.getId(BELL))
        .add(Registry.BLOCK.getId(PISTON))
        .add(Registry.BLOCK.getId(STICKY_PISTON))
        .add(Registry.BLOCK.getId(ACTIVATOR_RAIL))
        .add(Registry.BLOCK.getId(POWERED_RAIL))
        .add(Registry.BLOCK.getId(TNT)));
  }

  private void addButtons() {
    getOrCreateTagBuilder(BlockTags.BUTTONS).add(
      EXPOSED_COPPER_BUTTON, COPPER_BUTTON, WEATHERED_COPPER_BUTTON, OXIDIZED_COPPER_BUTTON
    );
  }

  private void addPlates() {
    getOrCreateTagBuilder(BlockTags.PRESSURE_PLATES).add(
      COPPER_PRESSURE_PLATE, EXPOSED_COPPER_PRESSURE_PLATE, WEATHERED_COPPER_PRESSURE_PLATE, OXIDIZED_COPPER_PRESSURE_PLATE
    );
  }


}
