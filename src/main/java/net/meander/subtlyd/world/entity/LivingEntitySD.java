package net.meander.subtlyd.world.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class LivingEntitySD extends LivingEntity {
    protected LivingEntitySD(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Handles a tent sleep attempt. Searches for players that are sleeping within the specified tent.
     * @param tent Tent to test
     */
    public static void startSleepingInTent(final Tent tent, final ServerPlayer player) {
        boolean foundSleepingPlayer = false;

        if (player.isPassenger()) {
            player.stopRiding();
        }

        for (Player anyplayer : PlayerLookup.tracking(tent)) {
            if (Tent.getTent(anyplayer, true) != null) {
                foundSleepingPlayer = true;
                break;
            }
        }

        tent.isOccupied = foundSleepingPlayer;
        player.setPose(Pose.SLEEPING);
        player.setYRot(tent.getYRot());
        player.setXRot(0.0F);
        setPosToTent(tent.blockPosition(), player);
        player.setSleepingPos(tent.blockPosition());
        player.setDeltaMovement(Vec3.ZERO);
        player.needsSync = true;
    }

    /**
     * Sets the sleeping position of a camping player.
     * @param blockPos The position of the tent.
     * @param player The player to move.
     */
    default void setPosToTent(BlockPos blockPos, ServerPlayer player) {
        player.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
    }
}
