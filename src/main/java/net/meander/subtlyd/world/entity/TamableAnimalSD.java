package net.meander.subtlyd.world.entity;

import net.minecraft.world.entity.TamableAnimal;

/**
 * @see TamableAnimal
 */
public interface TamableAnimalSD {
    double MAX_FOLLOW_DISTANCE_SQR = 400.0;

     default boolean shouldNotFollowOwner() {
        if (this instanceof TamableAnimal tamable && tamable.isTame() && tamable.getOwner() != null && !tamable.isInSittingPose()) {
            return !(tamable.distanceToSqr(tamable.getOwner()) < MAX_FOLLOW_DISTANCE_SQR);
        }

        return true;
    }
}
