package net.meander.subtlyd.world.level.block.sounds;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.world.level.biome.BiomeSD;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * @see net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer
 */
public class AmbientGrassyBlockSoundsPlayer {
    private static final int IDLE_SOUND_CHANCE_NIGHT = 250;
    private static final int IDLE_SOUND_CHANCE_DAY = 450;

    public static void playAmbientGrassSounds(Level level, BlockPos blockPos, RandomSource randomSource) {
        if (shouldPlayInsectSounds(level, blockPos, randomSource) && level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockPos) <= blockPos.getY()) {
            level.playPlayerSound(SoundEventsSD.GRASS_AMBIENT, SoundSource.AMBIENT, 0.3F, 1.0F);
        }
    }

    public static boolean shouldPlayInsectSounds(Level level, BlockPos blockPos, RandomSource randomSource) {
        if (!level.isRaining() && BiomeSD.getBiomeAsTemperatureVariant(level, blockPos) != TemperatureVariants.COLD && !level.getBiome(blockPos).is(Biomes.PALE_GARDEN)) {
            if (level.isDarkOutside() && randomSource.nextInt(IDLE_SOUND_CHANCE_NIGHT) == 0) {
                return true;
            } else {
                return level.isBrightOutside() && randomSource.nextInt(IDLE_SOUND_CHANCE_DAY) == 0;
            }
        }
        return false;
    }
}
