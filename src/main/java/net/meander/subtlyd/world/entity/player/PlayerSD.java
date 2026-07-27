package net.meander.subtlyd.world.entity.player;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @see Player
 */
public interface PlayerSD {
    default Either<TentSleepingProblem, Unit> startSleepInTent(final BlockPos pos) {
        if (this instanceof Player player) {
            player.startSleeping(pos);

            player.sleepCounter = 0;
        }

        return Either.right(Unit.INSTANCE);
    }

    record TentSleepingProblem(@Nullable Component message)  {
         public static final TentSleepingProblem TOO_FAR_AWAY = new TentSleepingProblem(Component.translatable("entity.subtlyd.tent.too_far_away"));
         public static final TentSleepingProblem OTHER_PROBLEM = new TentSleepingProblem(null);
         public static final TentSleepingProblem NOT_SAFE = new TentSleepingProblem(Component.translatable("block.minecraft.bed.not_safe"));
    }
}
