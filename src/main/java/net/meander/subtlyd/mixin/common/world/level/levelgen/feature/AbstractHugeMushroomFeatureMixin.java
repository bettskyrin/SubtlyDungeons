package net.meander.subtlyd.mixin.common.world.level.levelgen.feature;

import net.meander.subtlyd.tags.BiomeTagsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.HugeBrownMushroomFeature;
import net.minecraft.world.level.levelgen.feature.HugeRedMushroomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHugeMushroomFeature.class)
public class AbstractHugeMushroomFeatureMixin {

    @Inject(method = "place", at = @At("RETURN"))
    private void placeSmallMushroomCircleInDarkForest(FeaturePlaceContext<HugeMushroomFeatureConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            WorldGenLevel level = context.level();
            BlockPos origin = context.origin();

            if (level.getBiome(origin).is(BiomeTagsSD.HAS_CESPITOSE)) {
                AbstractHugeMushroomFeature feature = (AbstractHugeMushroomFeature) (Object) this;
                RandomSource random = context.random();
                BlockState smallMushroom;

                if (feature instanceof HugeRedMushroomFeature) {
                    smallMushroom = Blocks.RED_MUSHROOM.defaultBlockState();
                } else if (feature instanceof HugeBrownMushroomFeature) {
                    smallMushroom = Blocks.BROWN_MUSHROOM.defaultBlockState();
                } else {
                    smallMushroom = null;
                }

                if (smallMushroom != null) {
                    int patchCount = random.nextInt(4) + 3;
                    int radius = 4;

                    for (int i = 0; i < patchCount; i++) {
                        int offsetX = random.nextInt(radius * 3) - radius;
                        int offsetZ = random.nextInt(radius * 3) - radius;
                        BlockPos targetPos = origin.offset(offsetX, 2, offsetZ);

                        for (int y = 0; y < 5; y++) {
                            BlockPos placementPos = targetPos.below(y);

                            if (level.isEmptyBlock(placementPos) && smallMushroom.canSurvive(level, placementPos)) {
                                level.setBlock(placementPos, smallMushroom, 2);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}