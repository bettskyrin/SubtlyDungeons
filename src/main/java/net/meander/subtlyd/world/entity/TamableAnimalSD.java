package net.meander.subtlyd.world.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;

public class TamableAnimalSD {
    public static final double MAX_FOLLOW_DISTANCE_SQR = 400.0;

    public static boolean shouldFollowOwner(Mob mob) {
        if (mob instanceof TamableAnimal pet && pet.isTame() && pet.getOwner() != null && !pet.isInSittingPose()) {
            return pet.distanceToSqr(pet.getOwner()) < MAX_FOLLOW_DISTANCE_SQR;
        }
        return false;
    }
}
