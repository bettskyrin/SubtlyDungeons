package net.meander.subtlyd.world.entity.ai.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.function.BiPredicate;
import java.util.function.ToDoubleFunction;

/**
 * @see net.minecraft.world.entity.ai.util.LandRandomPos
 */
public class ShelteredRandomPos {
    public static @Nullable Vec3 getNearPos(final PathfinderMob mob, final int horizontalDist, final int verticalDist, BiPredicate<PathfinderMob, BlockPos> predicate) {
        return getNearPos(mob, horizontalDist, verticalDist, mob::getWalkTargetValue, predicate);
    }

    public static @Nullable Vec3 getNearPos(final PathfinderMob mob, final int horizontalDist, final int verticalDist, final ToDoubleFunction<BlockPos> positionWeight, BiPredicate<PathfinderMob, BlockPos> predicate) {
        return RandomPos.generateRandomPos(() -> ConditionalPos.generateNearConditionalPos(mob, horizontalDist, verticalDist, predicate), positionWeight);
    }
}
