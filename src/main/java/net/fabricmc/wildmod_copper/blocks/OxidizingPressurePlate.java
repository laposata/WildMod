package net.fabricmc.wildmod_copper.blocks;

import net.fabricmc.wildmod_copper.imixin.ICountInventory;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.Saddleable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;
import static net.minecraft.block.Oxidizable.OxidationLevel.UNAFFECTED;

public class OxidizingPressurePlate extends WeightedPressurePlateBlock {

  private final Oxidizable.OxidationLevel oxidized;

  private final int INVENTORY_COUNT = 37 * 64;

  private final int MAX_RSS;
  private final int ONE_RSS;
  private final float RSS_INTERVAL;
  private final List<Entity> counted;
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
    counted = new ArrayList<>();
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
    counted.clear();
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
    if(!counted.contains(entity)){
      counted.add(entity);
      if(entity instanceof PlayerEntity player){
        invCount = ((ICountInventory)player.getInventory()).getTotalInventory();
      } else if(entity instanceof InventoryOwner collector){
        invCount = ((ICountInventory)collector.getInventory()).getTotalInventory();
      } else if(entity instanceof VehicleInventory vehicle){
        invCount = ((ICountInventory)vehicle).getTotalInventory();
      } else if(entity instanceof AbstractHorseEntity vehicle){
        invCount = ((ICountInventory)vehicle).getTotalInventory();
      } else if(entity instanceof MobEntity mob){
        for(ItemStack stack: mob.getItemsHand()){
          invCount += stack.getCount() * (64 / stack.getMaxCount());
        }
        for(ItemStack stack: mob.getArmorItems()){
          invCount += stack.getCount() * (64 / stack.getMaxCount());
        }
        if(entity instanceof Saddleable){
          if(((Saddleable)entity).isSaddled()){
            invCount += 1;
          }
        }
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

}
