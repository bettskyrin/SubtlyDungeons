package com.kr1s1s.subtlyd.world.level.block.sounds;

import com.kr1s1s.subtlyd.sounds.SoundEventsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class AmbientBushBlockSoundsPlayer {
    public static void doBushSounds(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(70) == 0 && level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockPos) <= blockPos.getY()) {
            level.playLocalSound(blockPos, SoundEventsSD.BUSH_IDLE, SoundSource.AMBIENT, 1F, 1.0F, false);
        }
    }
}
