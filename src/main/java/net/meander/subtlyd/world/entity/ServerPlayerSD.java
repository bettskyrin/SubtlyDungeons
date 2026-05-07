package net.meander.subtlyd.world.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.meander.subtlyd.client.entity.player.PlayerSD;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributes;
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
     * @param pos Tent position
     * @param tent Tent the player is attempting to sleep in
     * @param player Player trying to sleep
     * @return Either a TentSleepingProblem or Unit if successful
     */
    public static Either<PlayerSD.TentSleepingProblem, Unit> startSleepInTent(BlockPos pos, TentEntity tent, ServerPlayer player) {
        if (!player.isSleeping() && player.isAlive()) {
            BedRule rule = player.level().environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, pos);
            boolean canSleep = rule.canSleep(player.level());

            if (!canSleep) {
                return Either.left(PlayerSD.TentSleepingProblem.NOT_POSSIBLE_HERE);
            } else if (tent.isOccupied()) {
                return Either.left(PlayerSD.TentSleepingProblem.OCCUPIED);
            } else if (!player.isWithinEntityInteractionRange(tent, -2)) {
                return Either.left(PlayerSD.TentSleepingProblem.TOO_FAR_AWAY);
            } else {
                if (player.level().isBrightOutside()) {
                    return Either.left(PlayerSD.TentSleepingProblem.NOT_POSSIBLE_NOW);
                } else {
                    if (!player.isCreative()) {
                        double hRange = 8.0;
                        double vRange = 5.0;
                        Vec3 vec3 = Vec3.atCenterOf(player.blockPosition());
                        List<Monster> list = player.level()
                                .getEntitiesOfClass(
                                        Monster.class,
                                        new AABB(vec3.x() - hRange, vec3.y() - vRange, vec3.z() - hRange, vec3.x() + hRange, vec3.y() + vRange, vec3.z() + hRange),
                                        monster -> monster.isPreventingPlayerRest(player.level(), player)
                                );
                        if (!list.isEmpty()) {
                            return Either.left(PlayerSD.TentSleepingProblem.NOT_SAFE);
                        }
                    }
                    Either<PlayerSD.TentSleepingProblem, Unit> either = PlayerSD.startSleepInTent(pos, tent, player);
                    player.level().updateSleepingPlayerList();
                    return either;
                }
            }
        }
        return Either.left(PlayerSD.TentSleepingProblem.OTHER_PROBLEM);
    }
}
