package net.fabricmc.wildmod_copper.mixin;

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
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.fabricmc.wildmod_copper.blocks.WildCopper.*;
import static net.fabricmc.wildmod_copper.utils.PropertyUtils.charged;

@Mixin(OxidizableBlock.class)
public class OxidizableMixin extends Block {

  private final AtomicBoolean powerful = new AtomicBoolean(true);
  private final AtomicBoolean placedAdjacent = new AtomicBoolean(false);

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
    return powerful.get() && charged(state) != 0;
  }

  @Override
  public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
    return WildCopper.getWeakRedstonePower(state, world, pos, direction, placedAdjacent);
  }

  public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
    return 0;
  }

  public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
    WildCopper.neighborUpdate(state, world, pos, sourceBlock, sourcePos, powerful, this);
  }

  public BlockState getPlacementState(ItemPlacementContext ctx) {
    int i = WildCopper.getReceivedRedstonePower(ctx.getWorld(), ctx.getBlockPos(), powerful);
    return getDefaultState().with(CHARGE, i).with(WET, checkIfWet(ctx.getBlockPos(), ctx.getWorld()));
  }

  public void tryWetDegrade(BlockState state, ServerWorld world, BlockPos pos, Random random) {
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
    float g = f * (float) Math.max((float) charged(state) / 12, .9) * ((OxidizableBlock) (Object) this).getDegradationChanceMultiplier();
    if (random.nextFloat() < g) {
      ((OxidizableBlock) (Object) this).getDegradationResult(state).ifPresent(s -> world.setBlockState(pos, s));
      WildCopper.update(world, pos, state, powerful, this);
    }
  }

  public void tryDegrade(BlockState state, ServerWorld world, BlockPos pos, Random random) {
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
    float g = f * Math.max((float) charged(state) / 12, f) * ((OxidizableBlock) (Object) this).getDegradationChanceMultiplier();
    if (random.nextFloat() < g) {
      ((OxidizableBlock) (Object) this).getDegradationResult(state).ifPresent(s -> world.setBlockState(pos, s));
      WildCopper.update(world, pos, state, powerful, this);
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
    }
  }

  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
      WildCopper.getStateForNeighborUpdate(direction, neighborState, world, pos, neighborPos, placedAdjacent);
      return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }


}
