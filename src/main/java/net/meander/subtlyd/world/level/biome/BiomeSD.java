package net.meander.subtlyd.world.level.biome;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class BiomeSD {
    /**
     * @return Whether a biome is considered cold.
     */
    public static Identifier getTemperatureAsVariantType(Level level, BlockPos blockPos) {
        if (level.getBiome(blockPos).value().coldEnoughToSnow(blockPos, level.getSeaLevel()) || level.precipitationAt(blockPos) == Biome.Precipitation.SNOW) {
            return TemperatureVariants.COLD;
        } else if (level.getBiome(blockPos).value().getBaseTemperature() >= 2.0) {
            if (level.dimension().equals(Level.OVERWORLD) && (level.isDarkOutside() || level.isWaterAt(blockPos))) {
                return TemperatureVariants.TEMPERATE;
            }
            return TemperatureVariants.WARM;
        } else {
            return TemperatureVariants.TEMPERATE;
        }
    }
    
    
}
