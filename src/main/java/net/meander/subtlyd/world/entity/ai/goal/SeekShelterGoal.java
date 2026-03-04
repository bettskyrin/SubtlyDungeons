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
        Vec3 foundShelterPos = this.findShelter();

        if (foundShelterPos != null) {
            this.shelterX = foundShelterPos.x;
            this.shelterY = foundShelterPos.y;
            this.shelterZ = foundShelterPos.z;
            return true;
        }
        return false;
    }

    /**
     * Searches within a 20 x 6 x 20 area for valid shelter.
     * @return A Vec3 shelter position or null, if valid shelter is not found.
     */
    private Vec3 findShelter() {
        BlockPos mobPos = this.mob.blockPosition();
        RandomSource random = this.mob.getRandom();

        for (int i = 0; i < 100; i++) {
            BlockPos randPos = mobPos.offset(random.nextInt(20) - 10,  random.nextInt(6) - 3, random.nextInt(20) - 10);

            if (!this.mob.level().isRainingAt(randPos) && this.mob.level().getBlockState(randPos).isPathfindable(PathComputationType.LAND)) {
                return Vec3.atBottomCenterOf(randPos);
            }
        }
        return null;
    }

    @Override
    public boolean canUse() {
        if (!this.mob.level().isRainingAt(this.mob.blockPosition())) {
            return false;
        }
        return setShelterPos();
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.level().isRaining();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.shelterX,  this.shelterY, this.shelterZ, this.speedModifier);
    }

    @Override
    public void tick() {
        if (this.mob.getNavigation().isDone()) {
            if (this.mob.level().isRainingAt(this.mob.blockPosition())) {
                if (this.setShelterPos()) {
                    start();
                }
            }
        }
    }
}
