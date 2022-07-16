package net.fabricmc.wildmod_copper.blocks;

import net.devtech.arrp.json.recipe.JIngredient;
import net.devtech.arrp.json.recipe.JKeys;
import net.devtech.arrp.json.recipe.JPattern;
import net.devtech.arrp.json.tags.JTag;
import net.fabricmc.wildmod_copper.blocks.states.PlateState;
import net.fabricmc.wildmod_copper.imixin.ICountInventory;
import net.fabricmc.wildmod_copper.imixin.ICraftable;
import net.fabricmc.wildmod_copper.utils.resource.generators.JHelper;
import net.fabricmc.wildmod_copper.utils.resource.generators.SimpleBlockResourceGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static net.fabricmc.wildmod_copper.WildModCopper.MINECRAFT_NAMESPACE;
import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;
import static net.fabricmc.wildmod_copper.utils.resource.generators.GenericResources.*;
import static net.minecraft.block.Oxidizable.OxidationLevel.UNAFFECTED;

public class OxidizingPressurePlate extends WeightedPressurePlateBlock implements ICraftable {

  private final Oxidizable.OxidationLevel oxidized;

  private final int INVENTORY_COUNT = 37 * 64;

  private final int MAX_RSS;
  private final int ONE_RSS;
  private final float RSS_INTERVAL;
  /**
   * Access widened by fabric-transitive-access-wideners-v1 to accessible
   *
   * @param settings
   */
  public OxidizingPressurePlate(Settings settings, Oxidizable.OxidationLevel oxidized) {
    super(oxidized.ordinal(), settings);
    this.oxidized = oxidized;
    this.MAX_RSS = (int)((this.oxidized.ordinal() + 1) / 2.0 * INVENTORY_COUNT);
    this.ONE_RSS = this.oxidized.ordinal() * 64;
    this.RSS_INTERVAL = (float) ((MAX_RSS - ONE_RSS) / 14.0);
  }


  @Override
  protected int getRedstoneOutput(World world, BlockPos pos) {
    List<Entity> entities = world.getNonSpectatingEntities(Entity.class, BOX.offset(pos))
                              .stream()
                              .filter(entity -> entity.isLiving() || entity.hasPassengers())
                              .toList();
    if(entities.size() == 0){
      return 0;
    }
    int totalInventory = entities
                           .stream()
                           .map(this::countEntitiesInventory)
                           .reduce(0, Integer::sum);
    return calcSignal(totalInventory);
  }

  private int calcSignal(int inventory){
    if(inventory >= MAX_RSS){
      return 15;
    }
    int roughLevel = (int)Math.ceil((inventory - ONE_RSS) / RSS_INTERVAL);
    return MathHelper.clamp(0, roughLevel, 15);
  }

  private int countEntitiesInventory(Entity entity){
    int invCount = 0;
    if(entity instanceof PlayerEntity player){
      invCount = ((ICountInventory)player.getInventory()).getTotalInventory();
    } else if(entity instanceof InventoryOwner collector){
      invCount = ((ICountInventory)collector.getInventory()).getTotalInventory();
    } else if(entity instanceof MobEntity mob){
      for(ItemStack stack: mob.getItemsHand()){

        invCount += stack.getCount() * (64 / stack.getMaxCount());
      }
      for(ItemStack stack: mob.getArmorItems()){
        invCount += stack.getCount() * (64 / stack.getMaxCount());
      }
    }
    if(entity.hasPassengers()){
      return invCount + entity.getPassengerList()
               .stream()
               .map(this::countEntitiesInventory)
               .reduce(0, Integer::sum);
    }
    return invCount;
  }

  public Identifier getIdentifier(){
    return new Identifier(NAMESPACE, getBlockName());
  }

  public String getBlockName(){
    return (oxidized == UNAFFECTED ? "" : oxidized.toString().toLowerCase(Locale.ROOT) + "_" ) + "copper_pressure_plate";
  }

  public void setRecipeAndTexture(){
    List<Item> blocks = WildCopper.byLevel.get(oxidized)
                       .stream()
                       .map(Block::asItem).collect(Collectors.toList());
    JTag tag = createIngredientTag(blocks, oxidized.name().toLowerCase()+ "_copper");

    String texture = oxidized.equals(UNAFFECTED)?
                       "copper_block"
                       : oxidized.toString().toLowerCase()+"_copper";

    addItemModel("minecraft:block/"+texture + "_inventory", getBlockName());
    addBlockModel("minecraft:block/pressure_plate_up", texture, getBlockName());
    addBlockModel("minecraft:block/pressure_plate_down", texture, getBlockName());

    new SimpleBlockResourceGenerator(SERVER_PACK).generateSimpleBlockLootTable(getIdentifier());
    crafting(
      getIdentifier(),
      9,
      JKeys.keys().key("x", JIngredient.ingredient().tag(tag.toString())),
      JPattern.pattern("  ", "xx" )
    );

    CLIENT_PACK.addResource(
      ResourceType.CLIENT_RESOURCES,
      new Identifier(NAMESPACE, "blockstates/" + getBlockName()+".json"),
      JHelper.serialize(PlateState.state(
        new Identifier(NAMESPACE, getBlockName() + "_down").toString(),
        new Identifier(NAMESPACE, getBlockName()).toString()
      ))
    );
  }

}
