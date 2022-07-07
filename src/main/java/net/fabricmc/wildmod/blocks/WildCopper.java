package net.fabricmc.wildmod.blocks;

import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Set;

import static net.fabricmc.wildmod.registry.Tags.CHARGEABLE_COPPER;
import static net.fabricmc.wildmod.registry.Tags.CONDUCTS_COPPER;
import static net.fabricmc.wildmod.utils.PropertyUtils.charged;
import static net.fabricmc.wildmod.utils.PropertyUtils.waterlogged;
import static net.fabricmc.wildmod.utils.TagUtils.blockIsIn;
import static net.minecraft.block.Blocks.*;

public class WildCopper extends OxidizableBlock {
    private static final Vec3d[] COLORS;
    public static final IntProperty CHARGE = IntProperty.of("charge", 0, 15);
    public static final BooleanProperty WET = BooleanProperty.of("wet");
    private boolean placedAdjacent = false;
    public WildCopper(Oxidizable.OxidationLevel oxidationLevel, Settings settings) {
        super(oxidationLevel, settings);
        setDefaultState(getStateManager().getDefaultState().with(CHARGE, 0).with(WET, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHARGE);
        builder.add(WET);
    }

    private void addPoweredParticles(World world, Random random, BlockPos pos, Vec3d color, Direction direction, Direction direction2, float f, float g) {
        float h = g - f;
        if (!(random.nextFloat() >= 0.2F * h)) {
            float i = 0.4375F;
            float j = f + h * random.nextFloat();
            double d = 0.5 + (double) (0.4375F * (float) direction.getOffsetX()) + (double) (j * (float) direction2.getOffsetX());
            double e = 0.5 + (double) (0.4375F * (float) direction.getOffsetY()) + (double) (j * (float) direction2.getOffsetY());
            double k = 0.5 + (double) (0.4375F * (float) direction.getOffsetZ()) + (double) (j * (float) direction2.getOffsetZ());
            world.addParticle(new DustParticleEffect(new Vec3f(color), 1.0F), (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + k, 0.0, 0.0, 0.0);
        }
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        int i = (Integer) state.get(CHARGE);
        if (i != 0) {
            Iterator var6 = Direction.Type.HORIZONTAL.iterator();

            while (var6.hasNext()) {
                Direction direction = (Direction) var6.next();
                this.addPoweredParticles(world, random, pos, COLORS[i], direction, Direction.UP, -0.5F, 0.5F);

            }

        }
    }

    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        BlockState otherState = world.getBlockState(pos.offset(direction.getOpposite()));
        Block other = otherState.getBlock();
        if(otherState.isOf(AIR)){
            placedAdjacent = true;
        }
        if(blockIsIn(other, CONDUCTS_COPPER) ||
                 blockIsIn(other, CHARGEABLE_COPPER)) {
            return charged(state);
        }
        return 0;
    }

    public int getRedstonePowerLevelForGates(BlockState self, BlockState consumer){
        int i = charged(self);
        if(consumer.isOf(COMPARATOR)){
            switch (((OxidizableBlock)self.getBlock()).getDegradationLevel()){
                case UNAFFECTED -> {return (i + 1) / 2;}
                case EXPOSED -> {return i / 2;}
                case WEATHERED -> {return i / 4;}
                case OXIDIZED -> {return i / 8;}
            }
        }
        return i;
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
        int i = this.getReceivedRedstonePower(world, pos);
        if ((Integer) charged(state) != i) {
            if (world.getBlockState(pos) == state) {
                world.setBlockState(pos, (BlockState) state.with(CHARGE, i), 2);
            }
        }
        if(placedAdjacent){
            boolean wet = checkIfWet(pos, world);
            if (wet != state.get(WET)) {
                world.setBlockState(pos, (BlockState) state.with(WET, wet), 2);
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

    private int getReceivedRedstonePower(World world, BlockPos pos) {
        int i = world.getReceivedStrongRedstonePower(pos);
        if (i < 15) {
            return Math.max(i,
                  Direction.stream()
                        .map(dir ->
                                   increasePower(world.getBlockState(pos.offset(dir)))
                        )
                        .max(Integer::compareTo)
                        .orElse(0));
        }
        return i;
    }

    private int increasePower(BlockState state) {
        if(state.isOf(REDSTONE_BLOCK)){
            return 15;
        }
        if(blockIsIn(state.getBlock(), CHARGEABLE_COPPER) && state.getBlock() instanceof WildCopper){
            return charged(state) - ((WildCopper)state.getBlock()).getDegradationLevel().ordinal() - 1;
        }
        return 0;
    }


    public BlockState getPlacementState(ItemPlacementContext ctx) {
        int i = this.getReceivedRedstonePower(ctx.getWorld(),  ctx.getBlockPos());
        return getDefaultState().with(CHARGE, i).with(WET, checkIfWet(ctx.getBlockPos(), ctx.getWorld()));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.tickDegradation(state, world, pos, random);
    }

    private boolean checkIfWet(BlockPos pos, World world){
        Set<BlockPos> set = Sets.newHashSet();
        set.add(pos.up());
        for (Direction direction : Direction.Type.HORIZONTAL) {
            set.add(pos.offset(direction));
        }
        for (BlockPos blockPos : set) {
            BlockState state = world.getBlockState(blockPos);
            if(state.isOf(WATER) ||
                     waterlogged(state)){
                return true;
            }
        }
        return false;
    }

    public void tryWetDegrade(BlockState state2, ServerWorld world, BlockPos pos, Random random) {
        BlockPos blockPos;
        int l;
        int i = ((Enum)this.getDegradationLevel()).ordinal();
        int matchedCopper = 0;
        int aboveCopper = 0;
        Iterator<BlockPos> iterator = BlockPos.iterateOutwards(pos, 4, 4, 4).iterator();
        while (iterator.hasNext() && (blockPos = iterator.next()).getManhattanDistance(pos) <= 4) {
            if (blockPos.equals(pos) || !(world.getBlockState(blockPos).getBlock() instanceof Degradable)) {
                continue;
            }
            Block block = world.getBlockState(blockPos).getBlock();
            Enum<OxidationLevel> enum_ = ((Degradable)(block)).getDegradationLevel();
            if (this.getDegradationLevel().getClass() != enum_.getClass()) continue;
            int m = (enum_).ordinal();
            if (m > i) {
                ++aboveCopper;
                continue;
            }
            ++matchedCopper;
        }
        float f = (float)(aboveCopper + 1) / (float)(aboveCopper + matchedCopper + 1);
        float g = f * (float)Math.max((float)charged(state2) / 12, .9) * this.getDegradationChanceMultiplier();
        if (random.nextFloat() < g) {
            this.getDegradationResult(state2).ifPresent(state -> world.setBlockState(pos, (BlockState)state));
        }
    }

    public void tryDegrade(BlockState state2, ServerWorld world, BlockPos pos, Random random) {
        BlockPos blockPos;
        int l;
        int i = ((Enum)this.getDegradationLevel()).ordinal();
        int matchedCopper = 0;
        int aboveCopper = 0;
        int belowCopper = 0;
        Iterator<BlockPos> iterator = BlockPos.iterateOutwards(pos, 4, 4, 4).iterator();
        while (iterator.hasNext() && (blockPos = iterator.next()).getManhattanDistance(pos) <= 4) {
            if (blockPos.equals(pos) || !(world.getBlockState(blockPos).getBlock() instanceof Degradable)) {
                continue;
            }
            Block block = world.getBlockState(blockPos).getBlock();
            Enum<OxidationLevel> enum_ = ((Degradable)(block)).getDegradationLevel();
            if (this.getDegradationLevel().getClass() != enum_.getClass()) continue;
            int m = (enum_).ordinal();
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
        float f = (float)(aboveCopper - belowCopper + 1) / (float)(aboveCopper + matchedCopper + 1);
        float g = f * Math.max((float)charged(state2) / 12, f) * this.getDegradationChanceMultiplier();
        if (random.nextFloat() < g) {
            this.getDegradationResult(state2).ifPresent(state -> world.setBlockState(pos, (BlockState)state));
        }
    }

    public void tickDegradation(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        float f = 0.05688889f;
        if (state.get(WET)){
            if (random.nextFloat() < f * 2) {
                this.tryWetDegrade(state, world, pos, random);
            }
        }
        if (random.nextFloat() < f) {
            this.tryDegrade(state, world, pos, random);
            this.update(world, pos, state);
        }
    }

    static {
        COLORS = (Vec3d[]) Util.make(new Vec3d[16], (vec3ds) -> {
            for (int i = 0; i <= 15; ++i) {
                float f = (float) i / 15.0F;
                float g = f * 0.6F + (f > 0.0F ? 0.4F : 0.3F);
                float h = MathHelper.clamp(f * f * 0.7F - 0.5F, 0.0F, 1.0F);
                float j = MathHelper.clamp(f * f * 0.6F - 0.7F, 0.0F, 1.0F);
                vec3ds[i] = new Vec3d((double) g, (double) h, (double) j);
            }
        });
    }

}
