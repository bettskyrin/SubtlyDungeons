package net.meander.subtlyd.world.level.block.sounds;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * @see net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer
 */
public class AmbientBushBlockSoundsPlayer {
    private static final int IDLE_SOUND_CHANCE = 70;

    public static void playAmbientBushSounds(Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(IDLE_SOUND_CHANCE) == 0 && level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockPos) <= blockPos.getY()) {
            level.playPlayerSound(SoundEventsSD.BUSH_IDLE, SoundSource.AMBIENT, 0.7F, 1.0F);
        }
    }
}
