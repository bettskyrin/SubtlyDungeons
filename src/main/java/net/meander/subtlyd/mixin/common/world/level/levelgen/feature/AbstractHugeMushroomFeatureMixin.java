package net.meander.subtlyd.mixin.common.world.level.levelgen.feature;

import net.meander.subtlyd.tags.BiomeTagsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.HugeBrownMushroomFeature;
import net.minecraft.world.level.levelgen.feature.HugeRedMushroomFeature;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHugeMushroomFeature.class)
public interface AbstractHugeMushroomFeatureMixin {
    @Inject(method = "place", at = @At("RETURN"))
    private void placeCespitose(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            if (level.getBiome(origin).is(BiomeTagsSD.HAS_CESPITOSE)) {
                AbstractHugeMushroomFeature feature = (AbstractHugeMushroomFeature) this;
                BlockState mushroom = getMushroom(feature);

                if (mushroom != null) {
                    int patchCount = random.nextInt(4) + 3;
                    int radius = 3;

                    for (int i = 0; i < patchCount; i++) {
                        int offsetX = random.nextInt(radius * 3) - radius;
                        int offsetZ = random.nextInt(radius * 3) - radius;
                        BlockPos targetPos = origin.offset(offsetX, 2, offsetZ);

                        for (int y = 0; y < 5; y++) {
                            BlockPos placementPos = targetPos.below(y);

                            if (level.isEmptyBlock(placementPos) && mushroom.canSurvive(level, placementPos)) {
                                level.setBlock(placementPos, mushroom, 2);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static @Nullable BlockState getMushroom(AbstractHugeMushroomFeature feature) {
        return switch (feature) {
            case HugeRedMushroomFeature _ -> Blocks.RED_MUSHROOM.defaultBlockState();
            case HugeBrownMushroomFeature _ -> Blocks.BROWN_MUSHROOM.defaultBlockState();
            case null, default -> null;
        };
    }
}