package net.fabricmc.wildmod_copper.blocks;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;

import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;
import static net.fabricmc.wildmod_copper.data_providers.collections.BlockTags.CHARGEABLE_COPPER;
import static net.fabricmc.wildmod_copper.utils.ParticleEffects.REDSTONE_EFFECT;
import static net.fabricmc.wildmod_copper.utils.ParticleEffects.SMOKE_EFFECT;
import static net.fabricmc.wildmod_copper.utils.PropertyUtils.charged;
import static net.fabricmc.wildmod_copper.utils.TagUtils.blockIsIn;
import static net.minecraft.block.Blocks.REDSTONE_BLOCK;
import static net.minecraft.block.Blocks.REDSTONE_WIRE;
import static net.minecraft.state.property.Properties.POWER;

public abstract class AbstractFuseBlock extends Block {
    public static final Map<Oxidizable.OxidationLevel, Integer> MAX_CHARGES = Map.of(
            Oxidizable.OxidationLevel.UNAFFECTED, 5,
            Oxidizable.OxidationLevel.EXPOSED, 8,
            Oxidizable.OxidationLevel.WEATHERED, 11,
            Oxidizable.OxidationLevel.OXIDIZED, 14
    );
    public static final Map<Oxidizable.OxidationLevel, IntProperty> POWER_LEVELS = Map.of(
            Oxidizable.OxidationLevel.UNAFFECTED, IntProperty.of("power", 0, 5),
            Oxidizable.OxidationLevel.EXPOSED, IntProperty.of("power", 0, 8),
            Oxidizable.OxidationLevel.WEATHERED, IntProperty.of("power", 0, 11),
            Oxidizable.OxidationLevel.OXIDIZED, IntProperty.of("power", 0, 14)
    );

    public static final Map<Oxidizable.OxidationLevel, String> BLOCK_ID = Map.of(
            Oxidizable.OxidationLevel.UNAFFECTED, "copper_fuse_block",
            Oxidizable.OxidationLevel.EXPOSED, "exposed_fuse_block",
            Oxidizable.OxidationLevel.WEATHERED, "weathered_fuse_block",
            Oxidizable.OxidationLevel.OXIDIZED, "oxidized_fuse_block"
    );
    public static final BooleanProperty BURNT_OUT = BooleanProperty.of("burnt_out");
    public static final ParticleEffect BURNT_OUT_SMOKE = SMOKE_EFFECT;
    public static final ParticleEffect POWER_SMOKE = REDSTONE_EFFECT;
    public final int maxCharge;
    public final Oxidizable.OxidationLevel oxideLevel;
    private boolean powerful = true;
    public abstract IntProperty POWER();
    public AbstractFuseBlock(Settings settings, Oxidizable.OxidationLevel oxideLevel) {
        super(settings);
        this.setDefaultState(getDefaultState().with(POWER(), 0).with(BURNT_OUT, false));
        this.maxCharge = MAX_CHARGES.get(oxideLevel);
        this.oxideLevel = oxideLevel;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BURNT_OUT);
        builder.add(POWER());
        super.appendProperties(builder);
    }

    public Identifier getId(){
        return new Identifier(NAMESPACE, BLOCK_ID.get(oxideLevel));
    }

    private int getPower(BlockState state){
        if(!powerful) return 0;
        if(state.isOf(this)){
            return state.get(BURNT_OUT) ? 0 : Math.max(state.get(POWER()) - 1, 0);
        } else if( state.getBlock() instanceof AbstractFuseBlock fuse){
            return fuse.getPower(state);
        }
        return 0;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return !state.get(BURNT_OUT) && powerful;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return getPower(state);
    }

    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        BlockState otherState = world.getBlockState(pos.offset(direction.getOpposite()));
        Block other = otherState.getBlock();
        if(other instanceof AbstractFuseBlock) {
            return 0;
        }
        return Math.max(0, getWeakRedstonePower(state, world, pos, direction) - 1);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) {
            return;
        }
        int bl2 = getReceivedRedstonePower((World) world, pos);
        int bl = state.get(POWER());
        if (bl2 != bl && !state.get(BURNT_OUT)) {
           updateNeighbors(pos, world);
        }
    }
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            int bl2 = getReceivedRedstonePower((World) world, pos);
            int bl = state.get(POWER());
            if (bl2 != bl) {
                return getNextBlockState(state, bl2, (World) world, pos);
            }
        }
        return state;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        updateNeighbors(pos, world);
        if(newState.isOf(this) && state.get(BURNT_OUT) != newState.get(BURNT_OUT)){
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        updateNeighbors(pos, world);
        if(state.get(BURNT_OUT)){
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
        }
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getPlacementState(ctx.getWorld(), ctx.getBlockPos());
    }

    private BlockState getPlacementState(World world, BlockPos pos) {
        int i = getReceivedRedstonePower(world, pos);
        return getNextBlockState(getDefaultState(), i, world, pos);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(state.get(BURNT_OUT) && state.get(POWER()) == 0){
            if(random.nextFloat() < 1.0 / (2 * oxideLevel.ordinal() + 2)){
                world.setBlockState(pos, state.with(BURNT_OUT, false));
            }
        }
    }

    private BlockState getNextBlockState(BlockState current, int nextPowerLevel, World world, BlockPos pos){
        if(nextPowerLevel > maxCharge){
            return current.with(BURNT_OUT, true).with(POWER(), maxCharge);
        }
        return current.with(POWER(), nextPowerLevel);
    }
    private int getReceivedRedstonePower(World world, BlockPos pos) {
        powerful = false;
        int i = world.getReceivedRedstonePower(pos);
        powerful = true;
        if (i < maxCharge) {
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
        if(state.getBlock() instanceof AbstractFuseBlock){
            return getPower(state);
        }
        if(state.isOf(REDSTONE_WIRE)){
            return state.get(RedstoneWireBlock.POWER);
        }
        return 0;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        boolean burntOut = state.get(BURNT_OUT);
        int power = state.get(POWER());
        if (burntOut) {
            spawnParticle(world, pos, random, BURNT_OUT_SMOKE, power > 0 ? 2 : 1);
        } else if (power > 0) {
            spawnParticle(world, pos, random, POWER_SMOKE,1);
        }
    }

    private void spawnParticle(World world, BlockPos pos, Random random, ParticleEffect particleEffects, int count){
        for(int i = 0; i < count; i ++){
            double d = (double)pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 1.1;
            double e = (double)pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 1.1;
            double f = (double)pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.1;
            world.addParticle(particleEffects, d, e, f, 0.1, 0.1, 0.1);
        }
    }
    private void updateNeighborsHard(BlockPos source, World world){
        Arrays.stream(Direction.values()).filter(dir -> {
            BlockState neighbor = world.getBlockState(source.offset(dir));
            return neighbor.isOpaque() && !(neighbor.getBlock() instanceof AbstractFuseBlock);
        }).forEach(dir -> {
            world.updateNeighborsExcept(source.offset(dir), this, dir.getOpposite());
            world.updateNeighbor(source.offset(dir), this, source);
        });
    }

    private void updateNeighboringFusesAndTransparent(BlockPos source, World world){
        Arrays.stream(Direction.values())
            .filter(dir -> {
                BlockState neighbor = world.getBlockState(source.offset(dir));
                return neighbor.getBlock() instanceof AbstractFuseBlock || !neighbor.isOpaque();
            }).forEach(dir -> world.updateNeighbor(source.offset(dir), this, source));

    }
    public void updateNeighbors(BlockPos source, World world){
        updateNeighboringFusesAndTransparent(source, world);
        updateNeighborsHard(source, world);
    }

}
