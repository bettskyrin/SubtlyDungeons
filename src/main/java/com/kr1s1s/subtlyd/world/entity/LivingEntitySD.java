package com.kr1s1s.subtlyd.world.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class LivingEntitySD extends LivingEntity {

    protected LivingEntitySD(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Handles a tent sleep attempt. Searches for players that are sleeping within the specified tent.
     * @param blockPos Tent location
     * @param tent Tent to test
     * @param player Sleeping player
     */
    public static void startSleepingInTent(BlockPos blockPos, TentEntity tent, ServerPlayer player) {
        boolean foundSleepingPlayer = false;

        if (player.isPassenger()) {
            player.stopRiding();
        }

        for (Player anyplayer : PlayerLookup.tracking(tent)) {
            if (TentEntity.getTent(anyplayer, true) != null) {
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

    /**
     * Sets the sleeping position of a camping player.
     * @param blockPos The position of the tent.
     * @param player The player to move.
     */
    private static void setPosToTent(BlockPos blockPos, ServerPlayer player) {
        player.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
    }

    @Override public @Nullable HumanoidArm getMainArm() {
        return null;
    }
}
