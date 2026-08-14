package net.meander.subtlyd.world.entity.ai.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.function.BiPredicate;

public class ConditionalPos {
    public static @Nullable BlockPos generateNearConditionalPos(final PathfinderMob mob, final int xzDist, final int yDist, final BiPredicate<PathfinderMob, BlockPos> predicate) {
        for (int searchRadius = 2; searchRadius <= xzDist; searchRadius += 2) {
            RandomSource random = mob.getRandom();
            searchRadius = random.nextBoolean() ? searchRadius : searchRadius + 2;

            for (int attempts = 0; attempts < searchRadius * Mth.sqrt(searchRadius); attempts++) {
                Vec3 randPos = DefaultRandomPos.getPos(mob, searchRadius, yDist);

                if (randPos != null) {
                    BlockPos pos = BlockPos.containing(randPos);

                    if (predicate.test(mob, pos)) {
                        return pos;
                    }
                }
            }
        }

        return null;
    }
}
