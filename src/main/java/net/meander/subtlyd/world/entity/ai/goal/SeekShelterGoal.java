package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.TamableAnimalSD;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SeekShelterGoal extends Goal {
    protected final PathfinderMob mob;
    protected final double speedModifier;
    protected double shelterX;
    protected double shelterY;
    protected double shelterZ;

    public SeekShelterGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Sets the target shelter position.
     * @return True if a shelter position has been set.
     */
    protected boolean setShelterPos() {
        Vec3 foundShelterPos = findShelter();

        if (foundShelterPos != null) {
            shelterX = foundShelterPos.x;
            shelterY = foundShelterPos.y;
            shelterZ = foundShelterPos.z;
            return true;
        }
        return false;
    }

    /**
     * Searches for valid shelter.
     * @return A Vec3 shelter position or null, if valid shelter is not found.
     */
    private Vec3 findShelter() {
        RandomSource random = mob.getRandom();
        int YRADIUS = 4;

        for (int searchRadius = 4; searchRadius <= 16; searchRadius += 4) {
            for (int j = 0; j < searchRadius * 6; j++) {
                BlockPos randPos = mob.blockPosition().offset(random.nextInt((searchRadius * 2) + 1) - searchRadius, random.nextInt((YRADIUS * 2) + 1) - YRADIUS, random.nextInt((searchRadius * 2) + 1) - searchRadius);

                if (!mob.level().isRainingAt(randPos) && mob.level().getBlockState(randPos).isPathfindable(PathComputationType.LAND)) {
                    return Vec3.atBottomCenterOf(randPos);
                }
            }
        }
        return null;
    }

    @Override
    public boolean canUse() {
        if (!mob.level().isRainingAt(mob.blockPosition()) || TamableAnimalSD.shouldFollowOwner(mob) || mob.getTarget() != null) {
            return false;
        }
        return setShelterPos();
    }

    @Override
    public boolean canContinueToUse() {
        if (!mob.level().isRaining() || TamableAnimalSD.shouldFollowOwner(mob) || mob.getTarget() != null) {
            return false;
        }
        return !mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(shelterX,  shelterY, shelterZ, speedModifier);
    }

    @Override
    public void tick() {
        if (mob.getNavigation().isDone() && mob.level().isRainingAt(mob.blockPosition()) && setShelterPos()) {
            start();
        }
    }
}
