package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.ai.util.GoalUtilsSD;
import net.meander.subtlyd.world.entity.ai.util.ShelteredRandomPos;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;

public class SeekShadeGoal extends MoveToBlockGoal {
    protected final PathfinderMob mob;

    public SeekShadeGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier, 16, 4);

        this.mob = mob;
    }

    private boolean shouldSeekShade() {
        Identifier climate = mob.level().getClimateAsTemperatureVariant(mob.blockPosition());

        return GoalUtilsSD.needsShade(mob) && climate == TemperatureVariants.WARM;
    }

    @Override
    public boolean canUse() {
        return shouldSeekShade() & super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (mob.canStroll()) {
            return super.canContinueToUse();
        }

        return false;
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos pos) {
        Level level = mob.level();

        return !level.canSeeSky(pos) && !level.isRaining();
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
}
