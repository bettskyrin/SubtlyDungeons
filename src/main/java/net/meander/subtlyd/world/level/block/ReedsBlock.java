package net.meander.subtlyd.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

public class ReedsBlock extends DoublePlantBlock implements BonemealableBlock, BonemealableAquaticPlant, LiquidBlockContainer {
    public static final EnumProperty<DoubleBlockHalf> HALF = DoublePlantBlock.HALF;

    protected ReedsBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean mayPlaceOn(final BlockState state, final BlockGetter level, final BlockPos pos) {
        return state.isFaceSturdy(level, pos, Direction.UP) && !state.is(Blocks.MAGMA_BLOCK);
    }

    @Override
    protected ItemStack getCloneItemStack(final LevelReader level, final BlockPos pos, final BlockState state, final boolean includeData) {
        return new ItemStack(BlocksSD.REEDS);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
        BlockState blockState = super.getStateForPlacement(context);

        if (blockState != null) {
            FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
            FluidState upperFluidState = context.getLevel().getFluidState(context.getClickedPos().above());

            if (fluidState.is(FluidTags.WATER) && upperFluidState.is(Fluids.EMPTY) && fluidState.getAmount() == 8) {
                return blockState;
            }
        }

        return null;
    }

    @Override
    protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState lowerState = level.getBlockState(pos.below());

            return lowerState.is(this) && lowerState.getValue(HALF) == DoubleBlockHalf.LOWER;
        } else {
            FluidState fluidState = level.getFluidState(pos);

            return super.canSurvive(state, level, pos) && fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8;
        }
    }

    @Override
    protected FluidState getFluidState(final BlockState state) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return Fluids.EMPTY.defaultFluidState();
        }

        return Fluids.WATER.getSource(false);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity livingEntity, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        return false;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, BonemealSource source) {
        return BonemealableAquaticPlant.hasSpreadableNeighbourPos(level, pos, state);

    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state, BonemealSource source) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state, BonemealSource source) {
        BonemealableAquaticPlant.findSpreadableNeighbourPos(level, pos, state)
                .ifPresent(blockPosX -> {
                    level.setBlockAndUpdate(blockPosX, defaultBlockState());
                    level.setBlockAndUpdate(blockPosX.above(), level.getBlockState(pos.above()));
                });
    }
}
