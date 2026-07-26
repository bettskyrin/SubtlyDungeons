package net.meander.subtlyd.server.level;

import com.mojang.datafixers.util.Either;
import net.meander.subtlyd.advancements.triggers.CriteriaTriggersSD;
import net.meander.subtlyd.stats.StatsSD;
import net.meander.subtlyd.world.attribute.BedRuleSD;
import net.meander.subtlyd.world.entity.decoration.Tent;
import net.meander.subtlyd.world.entity.player.PlayerSD;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * @see ServerPlayer
 */
public interface ServerPlayerSD {
    /**
     * Server handling for player tent sleep
     * @param tent Tent the player is attempting to sleep in
     * @param player Player trying to sleep
     * @return Either a TentSleepingProblem or Unit if successful
     */
    default Either<PlayerSD.TentSleepingProblem, Unit> startSleepInTent(final Tent tent, final BedRule rule) { // FIXME
        if (this instanceof ServerPlayer player) {
            if (!player.isSleeping() && player.isAlive()) {
                BlockPos pos = tent.blockPosition();
                ServerLevel level = player.level();

                boolean canSleep = rule.canSleep(level);

                if (!canSleep) {
                    return Either.left(BedRuleSD.asProblem(rule));
                }

                if (!player.isWithinEntityInteractionRange(tent, -2)) {
                    return Either.left(PlayerSD.TentSleepingProblem.TOO_FAR_AWAY);
                }

                if (!player.isCreative()) {
                    double hRange = 8.0;
                    double vRange = 5.0;
                    Vec3 bedCenter = Vec3.atBottomCenterOf(pos);
                    List<Monster> monsters = level
                            .getEntitiesOfClass(
                                    Monster.class,
                                    new AABB(bedCenter.x() - hRange, bedCenter.y() - vRange, bedCenter.z() - hRange, bedCenter.x() + hRange, bedCenter.y() + vRange, bedCenter.z() + hRange),
                                    monster -> monster.isPreventingPlayerRest(level, player)
                            );

                    if (!monsters.isEmpty()) {
                        return Either.left(PlayerSD.TentSleepingProblem.NOT_SAFE);
                    }
                }

                Either<PlayerSD.TentSleepingProblem, Unit> result = ((Player) this).startSleepInTent(tent.blockPosition()).ifRight(_ -> {
                    player.awardStat(StatsSD.SLEEP_IN_TENT);
                    CriteriaTriggersSD.SLEPT_IN_TENT.trigger(player);
                });

                if (!level.canSleepThroughNights()) {
                    player.sendOverlayMessage(Component.translatable("sleep.not_possible"));
                }

                level.updateSleepingPlayerList();
                return result;
            }
        }

        return Either.left(PlayerSD.TentSleepingProblem.OTHER_PROBLEM);
    }
}
