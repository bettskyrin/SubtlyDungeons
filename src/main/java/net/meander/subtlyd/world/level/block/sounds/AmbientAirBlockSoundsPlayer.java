package net.meander.subtlyd.world.level.block.sounds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.tags.BiomeTagsSD;
import net.meander.subtlyd.tags.BlockTagsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * @see AmbientDesertBlockSoundsPlayer
 */
@Environment(EnvType.CLIENT)
public class AmbientAirBlockSoundsPlayer {
    private static final int IDLE_SOUND_CHANCE = 4200;
    private static final int SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD = 3;
    private static final int SURROUNDING_BLOCKS_DISTANCE_HORIZONTAL_CHECK = 4;
    private static final int SURROUNDING_BLOCKS_DISTANCE_VERTICAL_CHECK = 5;
    private static final int HORIZONTAL_DIRECTIONS = 4;

    public static void playColdWindSounds(final Level level, final BlockPos blockPos, final RandomSource random) {
        if (level.getBiome(blockPos).is(BiomeTagsSD.IS_WINDY)) {
            if (level.getBlockState(blockPos.above()).is(Blocks.AIR) && !level.getBlockState(blockPos.below()).is(Blocks.AIR)) {
                if (random.nextInt(IDLE_SOUND_CHANCE) == 0 && shouldPlayColdWindSound(level, blockPos)) {
                    level.playPlayerSound(SoundEventsSD.WIND, SoundSource.AMBIENT, 0.7F, 1.0F);
                }
            }
        }
    }

    private static boolean shouldPlayColdWindSound(final Level level, final BlockPos blockPos) {
        int matchingBlocksFound = 0;
        int sidesChecked = 0;
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            mutableBlockPos.set(blockPos).move(direction, SURROUNDING_BLOCKS_DISTANCE_HORIZONTAL_CHECK);

            if (columnContainsTriggeringBlock(level, mutableBlockPos) && matchingBlocksFound++ >= SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD) {
                return true;
            }

            sidesChecked++;
            int remainingSides = HORIZONTAL_DIRECTIONS - sidesChecked;
            int potentialMatches = remainingSides + matchingBlocksFound;
            boolean canStillFindRequiredSoundTriggerBlocks = potentialMatches >= SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD;

            if (!canStillFindRequiredSoundTriggerBlocks) {
                return false;
            }
        }

        return false;
    }

    private static boolean columnContainsTriggeringBlock(final Level level, final BlockPos.MutableBlockPos mutablePos) {
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, mutablePos) - 1;

        if (Mth.abs(surfaceY - mutablePos.getY()) > SURROUNDING_BLOCKS_DISTANCE_VERTICAL_CHECK) {
            mutablePos.move(Direction.UP, 6);
            BlockState aboveBlockState = level.getBlockState(mutablePos);
            mutablePos.move(Direction.DOWN);

            for (int i = 0; i < 10; i++) {
                BlockState currentBlockState = level.getBlockState(mutablePos);

                if (aboveBlockState.isAir() && currentBlockState.is(BlockTagsSD.TRIGGERS_AMBIENT_WIND_BLOCK_SOUNDS)) {
                    return true;
                }

                aboveBlockState = currentBlockState;

                mutablePos.move(Direction.DOWN);
            }

            return false;
        } else {
            boolean hasAirAbove = level.getBlockState(mutablePos.setY(surfaceY + 1)).isAir();

            return hasAirAbove && level.getBlockState(mutablePos.setY(surfaceY)).is(BlockTagsSD.TRIGGERS_AMBIENT_WIND_BLOCK_SOUNDS);
        }
    }
}
