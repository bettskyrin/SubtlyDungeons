package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.EntityTypeSD;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;

public class SeekWarmthGoal extends MoveToBlockGoal {
    private final PathfinderMob mob;

    public SeekWarmthGoal(PathfinderMob mob, double speedModifier, int searchRange) {
        super(mob, speedModifier, searchRange, 4);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        Identifier variant = EntityTypeSD.getTemperatureVariantType(mob);
        BlockPos mobPos = this.mob.blockPosition();

        if (!isValidTarget(mob.level(), mobPos) && variant != TemperatureVariants.COLD &&
                (mob.level().getBiome(mobPos).value().coldEnoughToSnow(mobPos, mob.level().getSeaLevel()) || mob.level().precipitationAt(mobPos) == Biome.Precipitation.RAIN)) {
            return super.canUse();
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        Identifier variant = EntityTypeSD.getTemperatureVariantType(mob);

        if (variant != TemperatureVariants.COLD || isValidTarget(mob.level(), mob.blockPosition())) {
            return false;
        }
        return super.canContinueToUse();
    }

    /**
     * Determines if a target location is "warm" (has a brightness of 10 or higher).
     * @return Whether a valid target location has been found.
     */
    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        return level.getBrightness(LightLayer.BLOCK, pos) >= 10;
    }

    @Override
    public double acceptedDistance() {
        return 4.0;
    }
}
