package com.meander.subtlyd.world.block;

import com.mojang.serialization.MapCodec;
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
    public static final MapCodec<ReedsBlock> CODEC = simpleCodec(ReedsBlock::new);
    public static final EnumProperty<DoubleBlockHalf> HALF = DoublePlantBlock.HALF;

    @Override
    public MapCodec<ReedsBlock> codec() { return CODEC; }

    protected ReedsBlock(Properties properties) { super(properties); }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.isFaceSturdy(blockGetter, blockPos, Direction.UP) && !blockState.is(Blocks.MAGMA_BLOCK);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
        return new ItemStack(BlocksSD.REEDS);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = super.getStateForPlacement(blockPlaceContext);
        if (blockState != null) {
            FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
            FluidState fluidState2 = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos().above());
            if (fluidState.is(FluidTags.WATER) && fluidState2.is(Fluids.EMPTY) && fluidState.getAmount() == 8) {
                return blockState;
            }
        }

        return null;
    }

    @Override
    protected boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        if (blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState blockState2 = levelReader.getBlockState(blockPos.below());
            return blockState2.is(this) && blockState2.getValue(HALF) == DoubleBlockHalf.LOWER;
        } else {
            FluidState fluidState = levelReader.getFluidState(blockPos);
            return super.canSurvive(blockState, levelReader, blockPos) && fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8;
        }
    }

    @Override
    protected FluidState getFluidState(BlockState blockState) {
        if (blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
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
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return BonemealableAquaticPlant.hasSpreadableNeighbourPos(levelReader, blockPos, blockState);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        BonemealableAquaticPlant.findSpreadableNeighbourPos(serverLevel, blockPos, blockState)
                .ifPresent(blockPosx -> {
                    serverLevel.setBlockAndUpdate(blockPosx, this.defaultBlockState());
                    serverLevel.setBlockAndUpdate(blockPosx.above(), serverLevel.getBlockState(blockPos.above()));
                });
    }
}
