package net.meander.subtlyd.client.entity.player;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.meander.subtlyd.world.entity.LivingEntitySD;
import net.meander.subtlyd.world.entity.TentEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class PlayerSD extends Player {
    public PlayerSD(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    /**
     * Client handling for player tent sleep.
     * @param tent Tent the player is attempting to sleep in
     * @param player Sleeping player
     */
    public static Either<TentSleepingProblem, Unit> startSleepInTent(TentEntity tent, ServerPlayer player) {
        player.setRespawnPosition(null, false);
        LivingEntitySD.startSleepingInTent(tent, player);
        return Either.right(Unit.INSTANCE);
    }

    public enum TentSleepingProblem {
        NOT_POSSIBLE_HERE(Component.translatable("sleep.not_possible")),
        NOT_POSSIBLE_NOW(Component.translatable("block.minecraft.bed.no_sleep")),
        TOO_FAR_AWAY(Component.translatable("entity.subtlyd.tent.too_far_away")),
        OCCUPIED(Component.translatable("entity.subtlyd.tent.occupied")),
        OTHER_PROBLEM,
        NOT_SAFE(Component.translatable("block.minecraft.bed.not_safe"));

        @Nullable private final Component message;

        TentSleepingProblem() {
            message = null;
        }

        TentSleepingProblem(final @Nullable Component component) {
            message = component;
        }

        @Nullable public Component message() {
            return message;
        }
    }
}
