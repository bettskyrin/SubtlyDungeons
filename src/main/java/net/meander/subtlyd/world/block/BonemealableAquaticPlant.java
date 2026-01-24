package net.meander.subtlyd.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public interface BonemealableAquaticPlant {
    static boolean hasSpreadableNeighbourPos(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return getSpreadableNeighbourPos(Direction.Plane.HORIZONTAL.stream().toList(), levelReader, blockPos, blockState).isPresent();
    }

    static Optional<BlockPos> findSpreadableNeighbourPos(Level level, BlockPos blockPos, BlockState blockState) {
        return getSpreadableNeighbourPos(Direction.Plane.HORIZONTAL.shuffledCopy(level.getRandom()), level, blockPos, blockState);
    }

    private static Optional<BlockPos> getSpreadableNeighbourPos(List<Direction> list, LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        for (Direction direction : list) {
            BlockPos blockPos2 = blockPos.relative(direction);
            BlockPos blockPos3 = blockPos2.above();
            if (levelReader.isWaterAt(blockPos2) && blockState.canSurvive(levelReader, blockPos2) && levelReader.isEmptyBlock(blockPos3)) {
                return Optional.of(blockPos2);
            }
        }
        return Optional.empty();
    }
}
