package net.meander.subtlyd.world.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

public interface MobSD {
    default long getHuntingCooldownTicks() {
        return 20;
    }

    default void setHuntingCooldownTicks(long time) {}

    default double getPanicSpeed() {
        if (this instanceof Mob mob) {
            for (WrappedGoal wrappedGoal : mob.getGoalSelector().getAvailableGoals()) {
                if (wrappedGoal.getGoal() instanceof PanicGoal panicGoal) {
                    return panicGoal.speedModifier;
                }
            }
        }

        return 1.25;
    }
}
