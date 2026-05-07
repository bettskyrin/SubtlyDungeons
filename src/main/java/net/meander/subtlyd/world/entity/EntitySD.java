package net.meander.subtlyd.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntitySD {
    /**
     * Finds the nearest climbable wall for an entity.
     * @param entity The entity to use as a basis for the test.
     * @return The direction of the nearest wall to a climbing entity.
     */
    public static Direction getNearestWall(Entity entity) {
        if (entity != null) {
            Direction movementDir = entity.getMotionDirection();
            BlockPos blockPos = entity.blockPosition();
            if (!entity.level().getBlockState(blockPos.relative(movementDir)).getCollisionShape(entity.level(), blockPos).isEmpty()) {
                return movementDir;
            }

            for (Direction dir : Direction.Plane.HORIZONTAL) {
                if (!entity.level().getBlockState(blockPos.relative(dir)).getCollisionShape(entity.level(), blockPos).isEmpty()) {
                    return dir;
                }
            }
        }
        return null;
    }

    /**
     * Used for finding the rotation angle of a climbing entity.
     * @param climber The climbing entity.
     * @param nearestWall The nearest climbable wall.
     * @return The angle of the climbing entity relative to the wall (yaw) in degrees.
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public static float getClimberRotation(Entity climber, Direction nearestWall, float oldYaw) {
        float yaw = oldYaw;
        if (climber != null && nearestWall != null) {
            Vec3 vel = climber.getDeltaMovement();

            if (vel.lengthSqr() > 0.0F) {
                switch (nearestWall) {
                    case NORTH -> yaw = (float) Mth.atan2(-vel.x, vel.y);
                    case EAST ->  yaw = (float) Mth.atan2(-vel.z, vel.y);
                    case SOUTH ->  yaw = (float) Mth.atan2(vel.x, vel.y);
                    case WEST ->  yaw = (float) Mth.atan2(vel.z, vel.y);
                }
                yaw = (float) Math.toDegrees(yaw);
            }
        }
        return yaw;
    }
}
