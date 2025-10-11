package com.kr1s1s.subtlyd.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class LivingEntitySD extends LivingEntity {
    public Level level = this.level();
    public static TentEntity tentEntity;

    protected LivingEntitySD(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static void startSleepingSD(BlockPos blockPos, TentEntity tent, ServerPlayer player) {
        if (player.isPassenger()) {
            player.stopRiding();
        }

        BlockState blockState = player.level().getBlockState(blockPos);
        if (blockState.getBlock() instanceof BedBlock) {
            tent.setOccupied(true);
        }

        player.setPose(Pose.SLEEPING);
        setPosToTent(blockPos, player);
        player.setSleepingPos(blockPos);
        player.setDeltaMovement(Vec3.ZERO);
        player.hasImpulse = true;
        setTent(tent);
    }

    public static void setTent(TentEntity tent) {
        tentEntity = tent;
    }

    public static TentEntity getTent() {
        return tentEntity;
    }

    private static void setPosToTent(BlockPos blockPos, ServerPlayer player) {
        player.setPos(blockPos.getX() + 0.5, blockPos.getY() + 0.6875, blockPos.getZ() + 0.5);
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }
}
