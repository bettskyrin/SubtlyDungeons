package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.ai.util.GoalUtilsSD;
import net.meander.subtlyd.world.entity.ai.util.ShelteredRandomPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ShelteredRandomStrollGoal extends WaterAvoidingRandomStrollGoal {
    public ShelteredRandomStrollGoal(final PathfinderMob mob, final double speedModifier) {
        super(mob, speedModifier);
    }

    @Override
    protected @Nullable Vec3 getPosition() {
        return mob.getRandom().nextFloat() >= probability ? ShelteredRandomPos.getNearPos(mob, 8, 2, GoalUtilsSD::isMobSheltering) : super.getPosition();
    }

    @Override
    public boolean canUse() {
        if (forceTrigger || (!checkNoActionTime || mob.getNoActionTime() < 100) && mob.getRandom().nextInt(reducedTickDelay(interval)) == 0) {
            Vec3 pos = getPosition();

            if (pos != null) {
                wantedX = pos.x();
                wantedY = pos.y();
                wantedZ = pos.z();
                forceTrigger = false;

                return GoalUtilsSD.isMobSheltering(mob, mob.blockPosition());
            }
        }

        return false;
    }
}
