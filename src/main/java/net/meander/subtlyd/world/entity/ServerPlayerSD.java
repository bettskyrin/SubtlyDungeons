package net.meander.subtlyd.world.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.meander.subtlyd.advancements.triggers.CriteriaTriggersSD;
import net.meander.subtlyd.client.entity.player.PlayerSD;
import net.meander.subtlyd.stats.StatsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ServerPlayerSD extends ServerPlayer {
    public ServerPlayerSD(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ClientInformation clientInformation) {
        super(minecraftServer, serverLevel, gameProfile, clientInformation);
    }

    /**
     * Server handling for player tent sleep
     * @param tent Tent the player is attempting to sleep in
     * @param player Player trying to sleep
     * @return Either a TentSleepingProblem or Unit if successful
     */
    public static Either<PlayerSD.TentSleepingProblem, Unit> startSleepInTent(final Tent tent, final ServerPlayer player, final BedRule rule) {
        BlockPos pos = tent.blockPosition();

        if (!player.isSleeping() && player.isAlive()) {
            boolean canSleep = rule.canSleep(player.level());

            if (!canSleep) {
                return Either.left(PlayerSD.TentSleepingProblem.NOT_POSSIBLE_NOW);
            }

            if (!player.isWithinEntityInteractionRange(tent, -2)) {
                return Either.left(PlayerSD.TentSleepingProblem.TOO_FAR_AWAY);
            }

            if (!player.isCreative()) {
                double hRange = 8.0;
                double vRange = 5.0;
                Vec3 bedCenter = Vec3.atBottomCenterOf(pos);
                List<Monster> monsters = player.level()
                        .getEntitiesOfClass(
                                Monster.class,
                                new AABB(bedCenter.x() - hRange, bedCenter.y() - vRange, bedCenter.z() - hRange, bedCenter.x() + hRange, bedCenter.y() + vRange, bedCenter.z() + hRange),
                                monster -> monster.isPreventingPlayerRest(player.level(), player)
                        );
                if (!monsters.isEmpty()) {
                    return Either.left(PlayerSD.TentSleepingProblem.NOT_SAFE);
                }
            }

            Either<PlayerSD.TentSleepingProblem, Unit> result = PlayerSD.startSleepInTent(tent, player).ifRight(_ -> {
                player.awardStat(StatsSD.SLEEP_IN_TENT);
                CriteriaTriggersSD.SLEPT_IN_TENT.trigger(player);
            });

            if (!player.level().canSleepThroughNights()) {
                player.sendOverlayMessage(Component.translatable("sleep.not_possible"));
            }

            player.level().updateSleepingPlayerList();
            return result;
        } else {
            return Either.left(PlayerSD.TentSleepingProblem.OTHER_PROBLEM);
        }
    }
}
