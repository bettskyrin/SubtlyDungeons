package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.ai.util.GoalUtilsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

public class SeekShadeGoal extends MoveToBlockGoal {
    protected final PathfinderMob mob;

    public SeekShadeGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier, 16, 4);

        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return GoalUtilsSD.needsShade(mob) && !GoalUtilsSD.isShadedPos(mob.level(), mob.blockPosition()) && super.canUse();
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos pos) {
        return GoalUtilsSD.isShadedPos(mob.level(), mob.blockPosition());
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
