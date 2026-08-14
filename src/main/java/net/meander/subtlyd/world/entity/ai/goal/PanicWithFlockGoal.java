package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.tags.DamageTypeTagsSD;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class PanicWithFlockGoal extends PanicGoal {
    private Entity flockPanicSource;

    public PanicWithFlockGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier, DamageTypeTagsSD.CAUSES_FLOCK_PANIC);

    }

    public void triggerFlockPanic(Entity danger) {
        flockPanicSource = danger;
    }

    @Override
    public boolean canUse() {
        if (flockPanicSource != null) {
            return findRandomPosition();
        }

        return super.canUse();
    }

    @Override
    public void start() {
        super.start();

        flockPanicSource = null;
    }

    @Override
    protected boolean findRandomPosition() {
        Entity danger = flockPanicSource != null ? flockPanicSource : mob.getLastHurtByMob();

        if (danger != null) {
            Vec3 pos = DefaultRandomPos.getPosAway(mob, 16, 4, danger.position());

            if (pos != null) {
                posX = pos.x();
                posY = pos.y();
                posZ = pos.z();

                return  true;
            }
        }

        return false;
    }
}
