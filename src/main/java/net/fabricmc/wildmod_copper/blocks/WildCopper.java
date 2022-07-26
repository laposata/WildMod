package net.fabricmc.wildmod_copper.blocks;

import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.fabricmc.wildmod_copper.data_providers.collections.BlockTags.CHARGEABLE_COPPER;
import static net.fabricmc.wildmod_copper.data_providers.collections.BlockTags.CONDUCTS_COPPER;
import static net.fabricmc.wildmod_copper.utils.PropertyUtils.charged;
import static net.fabricmc.wildmod_copper.utils.PropertyUtils.waterlogged;
import static net.fabricmc.wildmod_copper.utils.TagUtils.blockIsIn;
import static net.minecraft.block.Block.dropStacks;
import static net.minecraft.block.Blocks.*;

public class WildCopper{
    public static final Vec3d[] COLORS;
    public static final IntProperty CHARGE = IntProperty.of("charge", 0, 15);
    public static final BooleanProperty WET = BooleanProperty.of("wet");

    public static final Map<Oxidizable.OxidationLevel, Set<Block>> byLevel = new HashMap<>();

    public static void registerOxidizable(Block block, Oxidizable.OxidationLevel level){
        Set<Block> set = WildCopper.byLevel.getOrDefault(level, new HashSet<>());
        set.add(block);
        WildCopper.byLevel.put(level, set);
    }
    public static int getRedstonePowerLevelForGates(BlockState self, BlockState consumer){
        int i = charged(self);
        if(i == 0){
            return 0;
        }
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

    public static boolean checkIfWet(BlockPos pos, World world){
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
    public static void addPoweredParticles(World world, Random random, BlockPos pos, Vec3d color, Direction direction, Direction direction2, float f, float g) {
        float h = g - f;
        if (!(random.nextFloat() >= 0.2F * h)) {
            float j = f + h * random.nextFloat();
            double d = 0.5 + (double) (0.4375F * (float) direction.getOffsetX()) + (double) (j * (float) direction2.getOffsetX());
            double e = 0.5 + (double) (0.4375F * (float) direction.getOffsetY()) + (double) (j * (float) direction2.getOffsetY());
            double k = 0.5 + (double) (0.4375F * (float) direction.getOffsetZ()) + (double) (j * (float) direction2.getOffsetZ());
            world.addParticle(new DustParticleEffect(new Vec3f(color), 1.0F), (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + k, 0.0, 0.0, 0.0);
        }
    }

    public static int getReceivedRedstonePower(World world, BlockPos pos, AtomicBoolean powerful) {
        powerful.set(false);
        int i = world.getReceivedStrongRedstonePower(pos);
        powerful.set(true);
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


    private static int increasePower(BlockState state) {
        if(state.isOf(REDSTONE_BLOCK)){
            return 15;
        }
        if(blockIsIn(state.getBlock(), CHARGEABLE_COPPER) && state.getBlock() instanceof OxidizableBlock){
            return charged(state) - ((OxidizableBlock)state.getBlock()).getDegradationLevel().ordinal() - 1;
        }
        return 0;
    }

    public static void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, AtomicBoolean powerful, Block self) {
        if (!world.isClient) {
            if (state.canPlaceAt(world, pos)) {
                update(world, pos, state, powerful, self);
            } else {
                dropStacks(state, world, pos);
                world.removeBlock(pos, false);
            }

        }
    }
    public static int getWeakRedstonePower(BlockState state, BlockView world,
                                           BlockPos pos, Direction direction, AtomicBoolean placedAdjacent){
        BlockState otherState = world.getBlockState(pos.offset(direction.getOpposite()));
        Block other = otherState.getBlock();
        if (blockIsIn(other, CONDUCTS_COPPER) || blockIsIn(other, CHARGEABLE_COPPER) ||
                placedAdjacent.get()) {
            placedAdjacent.set(false);
            return charged(state);
        }
        return 0;
    }
    public static void update(World world, BlockPos pos, BlockState state, AtomicBoolean powerful, Block self) {
        int i = WildCopper.getReceivedRedstonePower(world, pos, powerful);
        if (charged(state) != i) {
            if (world.getBlockState(pos) == state) {
                world.setBlockState(pos, state.with(CHARGE, i), 2);
            }
            updateAdjacentBlocks(pos, world, self);
        }
        boolean wet = checkIfWet(pos, world);
        if (wet != state.get(WET)) {
            world.setBlockState(pos, state.with(WET, wet), 2);
        }
    }

    private static void updateAdjacentBlocks(BlockPos pos, World world, Block self){
        Set<BlockPos> set = Sets.newHashSet();
        set.add(pos);
        for (Direction direction : Direction.values()) {
            set.add(pos.offset(direction));
        }
        for (BlockPos blockPos : set) {
            world.updateNeighborsAlways(blockPos, self);
        }
    }
    static {
        COLORS = Util.make(new Vec3d[16], (vec3ds) -> {
            for (int i = 0; i <= 15; ++i) {
                float f = (float) i / 15.0F;
                float g = f * 0.6F + (f > 0.0F ? 0.4F : 0.3F);
                float h = MathHelper.clamp(f * f * 0.7F - 0.5F, 0.0F, 1.0F);
                float j = MathHelper.clamp(f * f * 0.6F - 0.7F, 0.0F, 1.0F);
                vec3ds[i] = new Vec3d(g, h, j);
            }
        });
    }

    public static BlockState getStateForNeighborUpdate(Direction direction, BlockState neighborState,
                                                       WorldAccess world, BlockPos pos,
                                                       BlockPos neighborPos, AtomicBoolean placedAdjacent) {
        if(blockIsIn(neighborState.getBlock(), CONDUCTS_COPPER)){
            placedAdjacent.set(true);
        }
        return world.getBlockState(pos);
    }
}
