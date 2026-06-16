package net.meander.subtlyd.world.level.biome;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class BiomeSD {
    /**
     * @return A biome as a temperature variant by location.
     */
    public static Identifier getTemperatureAsVariantType(Level level, BlockPos blockPos) {
        if (level.precipitationAt(blockPos) == Biome.Precipitation.SNOW || level.getBiome(blockPos).value().coldEnoughToSnow(blockPos, level.getSeaLevel())) {
            return TemperatureVariants.COLD;
        } else if (level.getBiome(blockPos).value().getBaseTemperature() >= 2.0) {
            if (level.isDarkOutside() || level.isWaterAt(blockPos)) {
                return TemperatureVariants.TEMPERATE;
            }
            return TemperatureVariants.WARM;
        } else {
            return TemperatureVariants.TEMPERATE;
        }
    }
    
    
}
