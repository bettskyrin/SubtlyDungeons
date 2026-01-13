package com.kr1s1s.subtlyd.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
    public static final Logger LOGGER = LoggerFactory.getLogger("Subtly Dungeons");
    public static final String NAMESPACE = "subtlyd";

    /**
     * A quick log output method.
     */
    @SuppressWarnings("unused")
    public static void debug(Object logValue) {
        LOGGER.info("Debug: {}", logValue);
    }

    /**
     * @param string The path.
     * @return An identifier within the mod namespace
     */
    public static Identifier identifier(String string) {
        return Identifier.fromNamespaceAndPath(NAMESPACE, string);
    }

    public static class Logic {
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
        public static float getClimberRotation(Entity climber, Direction nearestWall) {
            float yaw = 0.0F;
            if (climber != null && nearestWall != null) {
                Vec3 vel = climber.getDeltaMovement();

                if (vel.lengthSqr() > 0.0F) {
                    switch (nearestWall) {
                        case NORTH -> yaw = (float) Mth.atan2(vel.x, vel.y);
                        case EAST ->  yaw = (float) Mth.atan2(vel.z, vel.y);
                        case SOUTH ->  yaw = (float) Mth.atan2(-vel.x, vel.y);
                        case WEST ->  yaw = (float) Mth.atan2(-vel.z, vel.y);
                    }
                    yaw = (float) Math.toDegrees(yaw);
                }
            }
            return yaw;
        }
    }

    public static class Globals {
        public static int BACK_BUTTON_WIDTH = 60;
    }
}