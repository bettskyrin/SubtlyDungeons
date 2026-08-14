package net.meander.subtlyd.world.entity.ai.util;

import net.meander.subtlyd.tags.EntityTypeTagsSD;
import net.meander.subtlyd.world.entity.EntityTypeSD;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

/**
 * @see net.minecraft.world.entity.ai.util.GoalUtils
 */
public class GoalUtilsSD {
    public static boolean needsRainShelter(Mob mob) {
        BlockPos pos = mob.blockPosition();

        return mob.level().isRaining()
                && !mob.level().getBiome(pos).value().coldEnoughToSnow(pos, mob.level().getSeaLevel());
    }

    public static boolean needsShade(Mob mob) {
        Identifier mobVariant = EntityTypeSD.getTemperatureVariantType(mob);
        BlockPos pos = mob.blockPosition();
        Level level = mob.level();

        return mobVariant != TemperatureVariants.WARM
                && level.dimensionType().hasSkyLight()
                && !level.isDarkOutside()
                && !level.isRaining()
                && !level.isWaterAt(pos)
                && level.getBiome(pos).value().getBaseTemperature() >= 2.0F;
    }

    public static boolean needsWarmth(Mob mob) {
        Identifier mobVariant = EntityTypeSD.getTemperatureVariantType(mob);
        BlockPos pos = mob.blockPosition();
        Level level = mob.level();

        return mobVariant != TemperatureVariants.COLD
                && level.getBiome(pos).value().coldEnoughToSnow(pos, level.getSeaLevel());
    }

    public static boolean isRainShelteredPos(Level level, BlockPos pos) {
        return !level.isRainingAt(pos);
    }

    public static boolean isShadedPos(Level level, BlockPos pos) {
        return !level.canSeeSky(pos);
    }

    public static boolean isWarmPos(Level level, BlockPos pos) {
        return level.getBrightness(LightLayer.BLOCK, pos) >= 10;
    }

    public static boolean isMobSheltering(Mob mob, BlockPos pos) {
        Level level = mob.level();

        return (mob.is(EntityTypeTagsSD.SEEKS_SHELTER) && needsRainShelter(mob) && isRainShelteredPos(level, pos))
                || (mob.is(EntityTypeTagsSD.SEEKS_SHADE) && needsShade(mob) && isShadedPos(level, pos))
                || (mob.is(EntityTypeTagsSD.SEEKS_WARMTH) && needsWarmth(mob) && isWarmPos(level, pos));
    }
}
