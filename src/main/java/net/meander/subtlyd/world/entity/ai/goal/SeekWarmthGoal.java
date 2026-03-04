package net.meander.subtlyd.world.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;

public class SeekWarmthGoal extends MoveToBlockGoal {
    private final PathfinderMob mob;

    public SeekWarmthGoal(PathfinderMob mob, double speedModifier, int searchRange) {
        super(mob, speedModifier, searchRange, 4);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        BlockPos mobPos = this.mob.blockPosition();

        if (this.mob.level().getBiome(mobPos).value().coldEnoughToSnow(mobPos, this.mob.level().getSeaLevel())) {
            return super.canUse();
        }
        return false;
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
