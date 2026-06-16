package net.meander.subtlyd.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
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

    /**
     * @param entity The entity to test.
     * @return Whether an entity should burn with the soul fire overlay
     */
    public static boolean shouldSoulFireBurn(Entity entity) {
        if (entity.is(EntityTypes.WITHER_SKULL) || entity.level().getBiome(entity.blockPosition()).is(Biomes.SOUL_SAND_VALLEY)) {
            return true;
        } else {
            final double SIZE_MODIFIER = 0.003;
            AABB bB = entity.getBoundingBox();
            BlockPos minPos = BlockPos.containing(bB.minX + SIZE_MODIFIER, bB.minY + SIZE_MODIFIER, bB.minZ + SIZE_MODIFIER);
            BlockPos maxPos = BlockPos.containing(bB.maxX - SIZE_MODIFIER, bB.maxY - SIZE_MODIFIER, bB.maxZ - SIZE_MODIFIER);

            for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                BlockState block = entity.level().getBlockState(pos);

                if (block.is(Blocks.SOUL_FIRE) || block.is(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
                    return true;
                }
            }
            return false;
        }
    }
}
