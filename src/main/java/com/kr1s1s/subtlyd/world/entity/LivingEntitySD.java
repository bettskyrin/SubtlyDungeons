package com.kr1s1s.subtlyd.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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

        for (Player otherPlayer : player.level().players()) {
            if (TentEntity.inTent(otherPlayer, tent) && otherPlayer.isSleeping()) {
                tent.setOccupied(true);
                break;
            } else {
                tent.setOccupied(false);
            }
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

    @Override public @Nullable HumanoidArm getMainArm() {
        return null;
    }
}
