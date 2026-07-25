package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.ai.util.GoalUtilsSD;
import net.meander.subtlyd.world.entity.ai.util.ShelteredRandomPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;

public class SeekShelterGoal extends MoveToBlockGoal {
    protected final PathfinderMob mob;

    public SeekShelterGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier, 32, 8);

        this.mob = mob;
    }

    private boolean shouldFindShelter() {
        return mob.level().isRainingAt(mob.blockPosition()) && GoalUtilsSD.needsShelterFromRain(mob);
    }

    @Override
    public boolean canUse() {
        return shouldFindShelter() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (mob.canStroll()) {
            return super.canContinueToUse();
        }

        return false;
    }

    @Override
    protected boolean findNearestBlock() {
        Vec3 pos = ShelteredRandomPos.getNearPos(mob, 16, 4, GoalUtilsSD::isSheltered);

        if (pos != null) {
            blockPos = BlockPos.containing(pos);

            return true;
        }

        return false;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        return !((Level) level).isRainingAt(pos) && level.getBlockState(pos).isSolidRender();
    }
}
