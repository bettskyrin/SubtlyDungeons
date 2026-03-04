package net.meander.subtlyd.world.entity.ai.goal;

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
     * Searches within a 20 x 6 x 20 area for valid shelter.
     * @return A Vec3 shelter position or null, if valid shelter is not found.
     */
    private Vec3 findShelter() {
        BlockPos mobPos = mob.blockPosition();
        RandomSource random = mob.getRandom();

        for (int i = 0; i < 100; i++) {
            BlockPos randPos = mobPos.offset(random.nextInt(20) - 10,  random.nextInt(6) - 3, random.nextInt(20) - 10);

            if (!mob.level().isRainingAt(randPos) && mob.level().getBlockState(randPos).isPathfindable(PathComputationType.LAND)) {
                return Vec3.atBottomCenterOf(randPos);
            }
        }
        return null;
    }

    @Override
    public boolean canUse() {
        if (!mob.level().isRainingAt(mob.blockPosition())) {
            return false;
        }
        return setShelterPos();
    }

    @Override
    public boolean canContinueToUse() {
        return mob.level().isRaining();
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(shelterX,  shelterY, shelterZ, speedModifier);
    }

    @Override
    public void tick() {
        if (mob.getNavigation().isDone()) {
            if (mob.level().isRainingAt(mob.blockPosition())) {
                if (setShelterPos()) {
                    start();
                }
            }
        }
    }
}
