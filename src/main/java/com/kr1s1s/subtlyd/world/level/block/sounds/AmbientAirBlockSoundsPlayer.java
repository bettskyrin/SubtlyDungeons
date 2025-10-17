package com.kr1s1s.subtlyd.world.level.block.sounds;

import com.kr1s1s.subtlyd.sounds.SoundEventsSD;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

@Environment(EnvType.CLIENT)
public class AmbientAirBlockSoundsPlayer {
    private static final int IDLE_SOUND_CHANCE = 2100;
    private static final int SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD = 3;
    private static final int SURROUNDING_BLOCKS_DISTANCE_HORIZONTAL_CHECK = 4;
    private static final int SURROUNDING_BLOCKS_DISTANCE_VERTICAL_CHECK = 5;
    private static final int HORIZONTAL_DIRECTIONS = 4;

    public static void playAmbientWindSounds(Level level, BlockPos blockPos, RandomSource randomSource) {
        if (level.getBlockState(blockPos.above()).is(Blocks.AIR) && !level.getBlockState(blockPos.below()).is(Blocks.AIR) && (level.getBiome(blockPos).is(Biomes.JAGGED_PEAKS) || level.getBiome(blockPos).is(BiomeTags.SPAWNS_SNOW_FOXES))) {
            if (randomSource.nextInt(IDLE_SOUND_CHANCE) == 0 && shouldPlayAmbientWindSound(level, blockPos)) {
                level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEventsSD.WIND, SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }
        }
    }

    private static boolean shouldPlayAmbientWindSound(Level level, BlockPos blockPos) {
        int i = 0;
        int j = 0;
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            mutableBlockPos.set(blockPos).move(direction, SURROUNDING_BLOCKS_DISTANCE_HORIZONTAL_CHECK);
            if (columnContainsTriggeringBlock(level, mutableBlockPos) && i++ >= SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD) {
                return true;
            }

            j++;
            int k = HORIZONTAL_DIRECTIONS - j;
            int l = k + i;
            boolean bl = l >= SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD;
            if (!bl) {
                return false;
            }
        }

        return false;
    }

    private static boolean columnContainsTriggeringBlock(Level level, BlockPos.MutableBlockPos mutableBlockPos) {
        int i = level.getHeight(Heightmap.Types.WORLD_SURFACE, mutableBlockPos) - 1;
        if (Math.abs(i - mutableBlockPos.getY()) > SURROUNDING_BLOCKS_DISTANCE_VERTICAL_CHECK) {
            mutableBlockPos.move(Direction.UP, 6);
            BlockState blockState = level.getBlockState(mutableBlockPos);
            mutableBlockPos.move(Direction.DOWN);

            for (int j = 0; j < 10; j++) {
                BlockState blockState2 = level.getBlockState(mutableBlockPos);
                if (blockState.isAir() && canTriggerAmbientWindSounds(blockState2)) {
                    return true;
                }

                blockState = blockState2;
                mutableBlockPos.move(Direction.DOWN);
            }

            return false;
        } else {
            boolean bl = level.getBlockState(mutableBlockPos.setY(i + 1)).isAir();
            return bl && canTriggerAmbientWindSounds(level.getBlockState(mutableBlockPos.setY(i)));
        }
    }

    private static boolean canTriggerAmbientWindSounds(BlockState blockState) {
        return blockState.is(Blocks.SNOW) || blockState.is(Blocks.STONE) || blockState.is(Blocks.CALCITE) || blockState.is(Blocks.PACKED_ICE);
    }
}
