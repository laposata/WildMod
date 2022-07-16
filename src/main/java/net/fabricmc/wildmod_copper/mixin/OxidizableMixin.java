package net.fabricmc.wildmod_copper.mixin;

import com.google.common.collect.Sets;
import net.fabricmc.wildmod_copper.blocks.WildCopper;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Set;

import static net.fabricmc.wildmod_copper.blocks.WildCopper.*;
import static net.fabricmc.wildmod_copper.registry.Tags.CHARGEABLE_COPPER;
import static net.fabricmc.wildmod_copper.registry.Tags.CONDUCTS_COPPER;
import static net.fabricmc.wildmod_copper.utils.PropertyUtils.charged;
import static net.fabricmc.wildmod_copper.utils.TagUtils.blockIsIn;
import static net.minecraft.block.Blocks.AIR;

@Mixin(OxidizableBlock.class)
public class OxidizableMixin extends Block {
  @Shadow @Final private Oxidizable.OxidationLevel oxidationLevel;
  private boolean placedAdjacent = false;

  private OxidizableMixin(Settings settings) {
    super(settings);
  }

  @Inject(at = @At("RETURN"), method = "<init>")
  public void constructor(Oxidizable.OxidationLevel oxidationLevel, Settings settings, CallbackInfo ci) {
    setDefaultState(stateManager.getDefaultState().with(CHARGE, 0).with(WET, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(CHARGE);
    builder.add(WET);
  }

  @Override
  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    int i = state.get(CHARGE);
    if (i != 0) {

      for (Direction direction : Direction.Type.HORIZONTAL) {
        WildCopper.addPoweredParticles(world, random, pos, WildCopper.COLORS[i], direction, Direction.UP, -0.5F, 0.5F);

      }

    }
  }

  @Override
  public boolean emitsRedstonePower(BlockState state) {
    return true;
  }

  @Override
  public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
    BlockState otherState = world.getBlockState(pos.offset(direction.getOpposite()));
    Block other = otherState.getBlock();
    if (otherState.isOf(AIR)) {
      placedAdjacent = true;
    }
    if (blockIsIn(other, CONDUCTS_COPPER) || blockIsIn(other, CHARGEABLE_COPPER)) {
      return charged(state);
    }
    return 0;
  }


  public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
    return 0;
  }

  public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
    if (!world.isClient) {
      if (state.canPlaceAt(world, pos)) {
        placedAdjacent = !blockIsIn(sourceBlock, CHARGEABLE_COPPER);
        this.update(world, pos, state);
      } else {
        dropStacks(state, world, pos);
        world.removeBlock(pos, false);
      }

    }
  }


  private void update(World world, BlockPos pos, BlockState state) {
    int i = WildCopper.getReceivedRedstonePower(world, pos);
    if (charged(state) != i) {
      if (world.getBlockState(pos) == state) {
        world.setBlockState(pos, state.with(CHARGE, i), 2);
      }
    }
    if (placedAdjacent) {
      boolean wet = checkIfWet(pos, world);
      if (wet != state.get(WET)) {
        world.setBlockState(pos, state.with(WET, wet), 2);
      }
      Set<BlockPos> set = Sets.newHashSet();
      set.add(pos);
      for (Direction direction : Direction.values()) {
        set.add(pos.offset(direction));
      }
      for (BlockPos blockPos : set) {
        world.updateNeighborsAlways(blockPos, this);
      }
      placedAdjacent = false;
    }
  }

  public BlockState getPlacementState(ItemPlacementContext ctx) {
    int i = WildCopper.getReceivedRedstonePower(ctx.getWorld(), ctx.getBlockPos());
    return getDefaultState().with(CHARGE, i).with(WET, checkIfWet(ctx.getBlockPos(), ctx.getWorld()));
  }

  public void tryWetDegrade(BlockState state2, ServerWorld world, BlockPos pos, Random random) {
    BlockPos blockPos;
    int l;
    int i = ((OxidizableBlock) (Object) this).getDegradationLevel().ordinal();
    int matchedCopper = 0;
    int aboveCopper = 0;
    Iterator<BlockPos> iterator = BlockPos.iterateOutwards(pos, 4, 4, 4).iterator();
    while (iterator.hasNext() && (blockPos = iterator.next()).getManhattanDistance(pos) <= 4) {
      if (blockPos.equals(pos) || !(world.getBlockState(blockPos).getBlock() instanceof Degradable)) {
        continue;
      }
      Block block = world.getBlockState(blockPos).getBlock();
      Enum enum_ = ((Degradable) (block)).getDegradationLevel();
      if (((OxidizableBlock) (Object) this).getDegradationLevel().getClass() != enum_.getClass()) continue;
      int m = (enum_).ordinal();
      if (m > i) {
        ++aboveCopper;
        continue;
      }
      ++matchedCopper;
    }
    float f = (float) (aboveCopper + 1) / (float) (aboveCopper + matchedCopper + 1);
    float g = f * (float) Math.max((float) charged(state2) / 12, .9) * ((OxidizableBlock) (Object) this).getDegradationChanceMultiplier();
    if (random.nextFloat() < g) {
      ((OxidizableBlock) (Object) this).getDegradationResult(state2).ifPresent(state -> world.setBlockState(pos, state));
    }
  }

  public void tryDegrade(BlockState state2, ServerWorld world, BlockPos pos, Random random) {
    BlockPos blockPos;
    int l;
    int i = ((OxidizableBlock) (Object) this).getDegradationLevel().ordinal();
    int matchedCopper = 0;
    int aboveCopper = 0;
    int belowCopper = 0;
    Iterator<BlockPos> iterator = BlockPos.iterateOutwards(pos, 4, 4, 4).iterator();
    while (iterator.hasNext() && (blockPos = iterator.next()).getManhattanDistance(pos) <= 4) {
      if (blockPos.equals(pos) || !(world.getBlockState(blockPos).getBlock() instanceof Degradable)) {
        continue;
      }
      Block block = world.getBlockState(blockPos).getBlock();
      Enum enum_ = ((Degradable) (block)).getDegradationLevel();
      if (((OxidizableBlock) (Object) this).getDegradationLevel().getClass() != enum_.getClass()) continue;
      int m = enum_.ordinal();
      if (m < i) {
        ++belowCopper;
        continue;
      }
      if (m > i) {
        ++aboveCopper;
        continue;
      }
      ++matchedCopper;
    }
    float f = (float) (aboveCopper - belowCopper + 1) / (float) (aboveCopper + matchedCopper + 1);
    float g = f * Math.max((float) charged(state2) / 12, f) * ((OxidizableBlock) (Object) this).getDegradationChanceMultiplier();
    if (random.nextFloat() < g) {
      ((OxidizableBlock) (Object) this).getDegradationResult(state2).ifPresent(state -> world.setBlockState(pos, state));
    }
  }

  @Override
  public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    this.tickDegradation(state, world, pos, random);
  }

  public void tickDegradation(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    float f = 0.05688889f;
    if (state.get(WET)) {
      if (random.nextFloat() < f * 2) {
        this.tryWetDegrade(state, world, pos, random);
      }
    }
    if (random.nextFloat() < f) {
      this.tryDegrade(state, world, pos, random);
      this.update(world, pos, state);
    }
  }

}
