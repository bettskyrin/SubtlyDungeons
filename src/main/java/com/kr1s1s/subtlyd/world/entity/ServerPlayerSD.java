package com.kr1s1s.subtlyd.world.entity;

import com.kr1s1s.subtlyd.client.entity.player.PlayerSD;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ServerPlayerSD extends ServerPlayer {
    public ServerPlayerSD(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ClientInformation clientInformation) {
        super(minecraftServer, serverLevel, gameProfile, clientInformation);
    }

    public static Either<PlayerSD.TentSleepingProblem, Unit> startSleepInTent(BlockPos blockPos, TentEntity tent, ServerPlayer player) {
            if (player.isSleeping() || !player.isAlive()) {
                return Either.left(PlayerSD.TentSleepingProblem.OTHER_PROBLEM);
            } else if (!player.level().dimensionType().natural()) {
                return Either.left(PlayerSD.TentSleepingProblem.NOT_POSSIBLE_HERE);
            } else if (tent.getOccupied()) {
                return Either.left(PlayerSD.TentSleepingProblem.OCCUPIED);
            } else if (!TentEntity.inTentRange(player)) {
                return Either.left(PlayerSD.TentSleepingProblem.TOO_FAR_AWAY);
            } else {
                player.setRespawnPosition(new RespawnConfig(LevelData.RespawnData.of(player.level().dimension(), blockPos, player.getYRot(), player.getXRot()), false), true);
                if (player.level().isBrightOutside()) {
                    return Either.left(PlayerSD.TentSleepingProblem.NOT_POSSIBLE_NOW);
                } else {
                    if (!player.isCreative()) {
                        double d = 8.0;
                        double e = 5.0;
                        Vec3 vec3 = player.blockPosition().getCenter();
                        List<Monster> list = player.level()
                                .getEntitiesOfClass(
                                        Monster.class,
                                        new AABB(vec3.x() - d, vec3.y() - e, vec3.z() - d, vec3.x() + d, vec3.y() + e, vec3.z() + d),
                                        monster -> monster.isPreventingPlayerRest(player.level(), player)
                                );
                        if (!list.isEmpty()) {
                            return Either.left(PlayerSD.TentSleepingProblem.NOT_SAFE);
                        }
                    }

                    Either<PlayerSD.TentSleepingProblem, Unit> either = PlayerSD.startSleepInTent(blockPos, tent, player);
                    player.level().updateSleepingPlayerList();
                    return either;
                }
            }
    }

    public static void stopSleepInTent(boolean bl, ServerPlayer player) {
        LivingEntitySD.getTent().setOccupied(false);
        if (bl) {
            player.level().updateSleepingPlayerList();
        }
        player.stopSleeping();
    }
}
