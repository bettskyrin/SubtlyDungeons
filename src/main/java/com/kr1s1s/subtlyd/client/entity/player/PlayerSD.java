package com.kr1s1s.subtlyd.client.entity.player;

import com.kr1s1s.subtlyd.world.entity.LivingEntitySD;
import com.kr1s1s.subtlyd.world.entity.TentEntity;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PlayerSD extends Player {
    public static int sleepCounter = 0;

    public PlayerSD(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    public static Either<TentSleepingProblem, Unit> startSleepInTent(BlockPos blockPos, TentEntity tent, ServerPlayer player) {
        player.setRespawnPosition(null, false);
        LivingEntitySD.startSleepingSD(blockPos, tent, player);
        player.level().updateSleepingPlayerList();
        sleepCounter = 0;
        return Either.right(Unit.INSTANCE);
    }

    @Override
    public @Nullable GameType gameMode() {
        return null;
    }

    public enum TentSleepingProblem {
        NOT_POSSIBLE_HERE(Component.translatable("sleep.not_possible")),
        NOT_POSSIBLE_NOW(Component.translatable("block.minecraft.bed.no_sleep")),
        TOO_FAR_AWAY(Component.translatable("entity.subtlyd.tent.too_far_away")),
        OCCUPIED(Component.translatable("entity.subtlyd.tent.occupied")),
        OTHER_PROBLEM,
        NOT_SAFE(Component.translatable("block.minecraft.bed.not_safe"));

        @Nullable
        private final Component message;

        TentSleepingProblem() {
            this.message = null;
        }

        TentSleepingProblem(final @Nullable Component component) {
            this.message = component;
        }

        @Nullable
        public Component getMessage() {
            return this.message;
        }
    }
}
