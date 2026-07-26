package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.ai.util.GoalUtilsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

public class MoveToShelterGoal extends MoveToBlockGoal {
    public MoveToShelterGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier, 16, 8);
    }

    @Override
    public boolean canUse() {
        return GoalUtilsSD.needsRainShelter(mob) && !GoalUtilsSD.isRainShelteredPos(mob.level(), mob.blockPosition()) && super.canUse();
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        return GoalUtilsSD.isRainShelteredPos((Level) level, pos.above());
    }
}
