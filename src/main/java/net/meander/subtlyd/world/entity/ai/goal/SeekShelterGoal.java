package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.TamableAnimalSD;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SeekShelterGoal extends Goal {
    protected final double speedModifier;
    protected double shelterX;
    protected double shelterY;
    protected double shelterZ;
    protected long nextStartTick;
    protected final PathfinderMob mob;

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
        int YRADIUS = 4;

        for (int searchRadius = 4; searchRadius <= 16; searchRadius += 4) {
            for (int j = 0; j < searchRadius * 6; j++) {
                Vec3 randPos = DefaultRandomPos.getPos(mob, searchRadius, YRADIUS);
                if (randPos != null && !mob.level().isRainingAt(BlockPos.containing(randPos))) {
                    return randPos;
                }
            }
        }
        return null;
    }

    @Override
    public boolean canUse() {
        if (mob.getNavigation().isInProgress() || mob.level().getGameTime() < nextStartTick || !mob.level().isRainingAt(mob.blockPosition())
                || TamableAnimalSD.shouldFollowOwner(mob) || mob.getTarget() != null) {
            return false;
        } else {
            boolean foundShelter = setShelterPos();

            if (!foundShelter) {
                nextStartTick = mob.level().getGameTime() + 100L + mob.getRandom().nextInt(10) * 20L;
            }
            return setShelterPos();
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!mob.level().isRaining() || TamableAnimalSD.shouldFollowOwner(mob) || mob.getTarget() != null) {
            return false;
        } else if (mob.getNavigation().isDone()) {
            return !mob.level().isRainingAt(mob.blockPosition());
        }
        return true;
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(shelterX,  shelterY, shelterZ, speedModifier);
    }

    @Override
    public void tick() {
        if (mob.getNavigation().isDone() && !mob.level().isRainingAt(mob.blockPosition())) {
            if (mob.getRandom().nextInt(120) == 0) {

                Vec3 randomPos = DefaultRandomPos.getPos(mob, 4, 3);

                if (randomPos != null && !mob.level().isRainingAt(BlockPos.containing(randomPos))) {
                    mob.getNavigation().moveTo(randomPos.x, randomPos.y, randomPos.z, speedModifier * 0.5);
                }
            }
        }
    }
}
