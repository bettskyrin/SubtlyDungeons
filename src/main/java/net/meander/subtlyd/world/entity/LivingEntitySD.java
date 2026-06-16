package net.meander.subtlyd.world.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class LivingEntitySD extends LivingEntity {
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
    }

    /**
     * Sets the sleeping position of a camping player.
     * @param blockPos The position of the tent.
     * @param player The player to move.
     */
    private static void setPosToTent(BlockPos blockPos, ServerPlayer player) {
        player.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
    }

    /**
     * @param mob The pathfinding mob to test.
     * @return The speed multiplier for that animal type when it panics.
     */
    public static double getPanicSpeed(PathfinderMob mob) {
        for (WrappedGoal wrappedGoal : mob.goalSelector.getAvailableGoals()) {
            if (wrappedGoal.getGoal() instanceof PanicGoal panicGoal) {
                return panicGoal.speedModifier;
            }
        }
        return 1.25D;
    }
}
