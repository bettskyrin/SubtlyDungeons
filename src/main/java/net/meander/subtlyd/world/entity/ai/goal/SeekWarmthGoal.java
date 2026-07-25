package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.ai.util.GoalUtilsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;

public class SeekWarmthGoal extends MoveToBlockGoal {
    protected final PathfinderMob mob;

    public SeekWarmthGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier, 32, 16);

        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return GoalUtilsSD.needsWarmth(mob) && !GoalUtilsSD.isWarmPos(mob.level(), mob.blockPosition()) && super.canUse();
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        return level.getBrightness(LightLayer.BLOCK, pos) >= 10;
    }

    @Override
    public double acceptedDistance() {
        return 4.0;
    }
}
