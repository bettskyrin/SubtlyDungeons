package com.kr1s1s.subtlyd.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LivingEntitySD extends LivingEntity {

    protected LivingEntitySD(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Handles a tent sleep attempt.
     * @param blockPos Tent location
     * @param tent Tent player is sleeping in
     * @param player Sleeping player
     */
    public static void startSleepingInTent(BlockPos blockPos, TentEntity tent, ServerPlayer player) {
        if (player.isPassenger()) {
            player.stopRiding();
        }

        boolean foundSleepingPlayer = false;
        for (Player anyPlayer : player.level().players()) {
            if (TentEntity.inTent(anyPlayer, tent) && anyPlayer.isSleeping()) {
                foundSleepingPlayer = true;
                break;
            }
        }
        tent.occupied = foundSleepingPlayer;
        player.setPose(Pose.SLEEPING);
        player.setYRot(tent.getYRot());
        player.setXRot(0.0F);
        setPosToTent(blockPos, player);
        player.setSleepingPos(blockPos);
        player.setDeltaMovement(Vec3.ZERO);
        player.setIgnoreFallDamageFromCurrentImpulse(true);
    }

    public void stopSleepingSD() {
        this.getSleepingPos().filter(this.level()::isLoaded).ifPresent(blockPos -> {
            List<TentEntity> tents = this.level().getEntitiesOfClass(TentEntity.class, new AABB(blockPos).inflate(1.0));
            for (TentEntity tent : tents) {
                tent.occupied = false;
            }

            if (TentEntity.inTentRange(this)) {
                super.stopSleeping();
            }
        });
    }

    private static void setPosToTent(BlockPos blockPos, ServerPlayer player) {
        player.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
    }

    @Override public @Nullable HumanoidArm getMainArm() {
        return null;
    }
}
