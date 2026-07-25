package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.ai.util.GoalUtilsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

public class SeekShelterGoal extends MoveToBlockGoal {
    protected final PathfinderMob mob;

    public SeekShelterGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier, 32, 8);

        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return GoalUtilsSD.needsRainShelter(mob) && !GoalUtilsSD.isRainShelteredPos(mob.level(), mob.blockPosition()) && super.canUse();
    }


    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        return GoalUtilsSD.isRainShelteredPos(mob.level(), pos);
    }

    @Override
    protected boolean findNearestBlock() {
        BlockPos mobPos = mob.blockPosition();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int y = verticalSearchStart; y <= verticalSearchRange; y = y > 0 ? -y : 1 - y) {
            for (int r = 0; r < searchRange; r++) {
                for (int x = 0; x <= r; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < r && x > -r ? r : 0; z <= r; z = z > 0 ? -z : 1 - z) {
                        pos.setWithOffset(mobPos, x, y - 1, z);

                        if (mob.isWithinHome(pos) && isValidTarget(mob.level(), pos)) {
                            blockPos = pos;

                            return true;
                        }
                    }
                }
            }
        }

        return true;
    }
}
