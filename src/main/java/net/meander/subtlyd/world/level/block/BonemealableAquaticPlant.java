package net.meander.subtlyd.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public interface BonemealableAquaticPlant {
    static boolean hasSpreadableNeighbourPos(final LevelReader level, final BlockPos pos, final BlockState state) {
        return getSpreadableNeighbourPos(Direction.Plane.HORIZONTAL.stream().toList(), level, pos, state).isPresent();
    }

    static Optional<BlockPos> findSpreadableNeighbourPos(Level level, final BlockPos pos, final BlockState state) {
        return getSpreadableNeighbourPos(Direction.Plane.HORIZONTAL.shuffledCopy(level.getRandom()), level, pos, state);
    }

    private static Optional<BlockPos> getSpreadableNeighbourPos(final List<Direction> directions, final LevelReader level, final BlockPos blockPos, final BlockState state) {
        for (Direction direction : directions) {
            BlockPos targetPos = blockPos.relative(direction);
            BlockPos abovePos = targetPos.above();

            if (level.isWaterAt(targetPos) && state.canSurvive(level, targetPos) && level.isEmptyBlock(abovePos)) {
                return Optional.of(targetPos);
            }
        }

        return Optional.empty();
    }
}
