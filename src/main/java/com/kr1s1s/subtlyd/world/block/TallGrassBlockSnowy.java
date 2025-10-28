package com.kr1s1s.subtlyd.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;

public class TallGrassBlockSnowy extends TallGrassBlock {
    public TallGrassBlockSnowy(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(SNOWY, false).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, SNOWY);
    }

    @Override
    public BlockState updateShape(BlockState blockState,
                                  LevelReader levelReader,
                                  ScheduledTickAccess scheduledTickAccess,
                                  BlockPos blockPos,
                                  Direction direction,
                                  BlockPos blockPos2,
                                  BlockState blockState2,
                                  RandomSource randomSource) {
        DoubleBlockHalf half = blockState.getValue(HALF);
        if (half == DoubleBlockHalf.LOWER) {
            boolean isSnowy = levelReader.getBlockState(blockPos.north()).is(Blocks.SNOW) || levelReader.getBlockState(blockPos.east()).is(Blocks.SNOW) || levelReader.getBlockState(blockPos.east()).is(Blocks.SNOW) || levelReader.getBlockState(blockPos.west()).is(Blocks.SNOW);
            return blockState.setValue(SNOWY, isSnowy);
        } else {
            BlockState below = levelReader.getBlockState(blockPos.below());
            if (below.is(this) && below.hasProperty(SNOWY)) {
                return blockState.setValue(SNOWY, below.getValue(SNOWY));
            }
        }
        return super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2, randomSource);
    }
}
