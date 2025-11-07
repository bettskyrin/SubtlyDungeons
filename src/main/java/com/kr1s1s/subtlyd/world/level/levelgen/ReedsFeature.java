package com.kr1s1s.subtlyd.world.level.levelgen;

import com.kr1s1s.subtlyd.world.block.BlocksSD;
import com.kr1s1s.subtlyd.world.block.ReedsBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class ReedsFeature extends Feature<ProbabilityFeatureConfiguration> {
    public ReedsFeature(Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> featurePlaceContext) {
        boolean bl = false;
        RandomSource randomSource = featurePlaceContext.random();
        WorldGenLevel worldGenLevel = featurePlaceContext.level();
        BlockPos blockPos = featurePlaceContext.origin();
        ProbabilityFeatureConfiguration probabilityFeatureConfiguration = featurePlaceContext.config();
        int i = randomSource.nextInt(8) - randomSource.nextInt(8);
        int j = randomSource.nextInt(8) - randomSource.nextInt(8);
        int k = worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, blockPos.getX() + i, blockPos.getZ() + j);
        BlockPos blockPos2 = new BlockPos(blockPos.getX() + i, k, blockPos.getZ() + j);
        if (worldGenLevel.getBlockState(blockPos2).is(Blocks.WATER)) {
            boolean bl2 = randomSource.nextDouble() < probabilityFeatureConfiguration.probability;
            BlockState blockState = BlocksSD.REEDS.defaultBlockState();
            if (blockState.canSurvive(worldGenLevel, blockPos2)) {
                if (bl2) {
                    BlockState blockState2 = blockState.setValue(ReedsBlock.HALF, DoubleBlockHalf.UPPER);
                    BlockPos blockPos3 = blockPos2.above();
                    if (worldGenLevel.getBlockState(blockPos3).is(Blocks.WATER)) {
                        worldGenLevel.setBlock(blockPos2, blockState, 2);
                        worldGenLevel.setBlock(blockPos3, blockState2, 2);
                    }
                } else {
                    worldGenLevel.setBlock(blockPos2, blockState, 2);
                }

                bl = true;
            }
        }

        return bl;
    }
}
