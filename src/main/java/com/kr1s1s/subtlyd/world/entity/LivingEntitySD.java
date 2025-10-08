package com.kr1s1s.subtlyd.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

public class LivingEntitySD extends LivingEntity {
    public Level level = this.level();
    protected LivingEntitySD(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static void startSleepingTent(TentEntity tent, Player player) {
        if (player.isPassenger()) {
            player.stopRiding();
        }

        BlockPos blockPos = tent.blockPosition();
        tent.occupied = true;

        player.setPose(Pose.SLEEPING);
        player.setSleepingPos(blockPos);
        player.setDeltaMovement(Vec3.ZERO);
        player.hasImpulse = true;
    }

    public static Optional<Vec3> findStandUpPosition(EntityType<?> entityType, CollisionGetter collisionGetter, BlockPos blockPos, Direction direction, float f) {
        Direction direction2 = direction.getClockWise();
        Direction direction3 = direction2.isFacingAngle(f) ? direction2.getOpposite() : direction2;
        int[][] is = tentStandUpOffsets(direction, direction3);
        Optional<Vec3> optional = findStandUpPositionAtOffset(entityType, collisionGetter, blockPos, is, true);

        return optional.isPresent() ? optional : findStandUpPositionAtOffset(entityType, collisionGetter, blockPos, is, false);
    }

    private static int[][] tentStandUpOffsets(Direction direction, Direction direction2) {
        return ArrayUtils.addAll((int[][]) tentSurroundStandUpOffsets(direction, direction2), (int[][]) tentAboveStandUpOffsets(direction));
    }

    private static int[][] tentSurroundStandUpOffsets(Direction direction, Direction direction2) {
        return new int[][]{
                {direction2.getStepX(), direction2.getStepZ()},
                {direction2.getStepX() - direction.getStepX(), direction2.getStepZ() - direction.getStepZ()},
                {direction2.getStepX() - direction.getStepX() * 2, direction2.getStepZ() - direction.getStepZ() * 2},
                {-direction.getStepX() * 2, -direction.getStepZ() * 2},
                {-direction2.getStepX() - direction.getStepX() * 2, -direction2.getStepZ() - direction.getStepZ() * 2},
                {-direction2.getStepX() - direction.getStepX(), -direction2.getStepZ() - direction.getStepZ()},
                {-direction2.getStepX(), -direction2.getStepZ()},
                {-direction2.getStepX() + direction.getStepX(), -direction2.getStepZ() + direction.getStepZ()},
                {direction.getStepX(), direction.getStepZ()},
                {direction2.getStepX() + direction.getStepX(), direction2.getStepZ() + direction.getStepZ()}
        };
    }

    private static int[][] tentAboveStandUpOffsets(Direction direction) {
        return new int[][]{{0, 0}, {-direction.getStepX(), -direction.getStepZ()}};
    }

    private static Optional<Vec3> findStandUpPositionAtOffset(EntityType<?> entityType, CollisionGetter collisionGetter, BlockPos blockPos, int[][] is, boolean bl) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int[] js : is) {
            mutableBlockPos.set(blockPos.getX() + js[0], blockPos.getY(), blockPos.getZ() + js[1]);
            Vec3 vec3 = DismountHelper.findSafeDismountLocation(entityType, collisionGetter, mutableBlockPos, bl);
            if (vec3 != null) {
                return Optional.of(vec3);
            }
        }

        return Optional.empty();
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }
}
