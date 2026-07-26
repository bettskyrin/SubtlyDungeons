package net.meander.subtlyd.client.entity.player;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.meander.subtlyd.world.entity.LivingEntitySD;
import net.meander.subtlyd.world.entity.Tent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractBedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * @see Player
 */
public interface PlayerSD {
    default Either<TentSleepingProblem, Unit> startSleepInTent(BlockPos pos) { // FIXME
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
