package net.meander.subtlyd.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.block.ReedsBlock;
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
        boolean placedAny = false;
        RandomSource random = featurePlaceContext.random();
        WorldGenLevel worldGenLevel = featurePlaceContext.level();
        BlockPos origin = featurePlaceContext.origin();
        int x = random.nextInt(8) - random.nextInt(8);
        int z = random.nextInt(8) - random.nextInt(8);
        int y = worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, origin.getX() + x, origin.getZ() + z);
        BlockPos reedsPos = new BlockPos(origin.getX() + x, y, origin.getZ() + z);

        if (worldGenLevel.getBlockState(reedsPos).is(Blocks.WATER)) {
            BlockState state = BlocksSD.REEDS.defaultBlockState();
            if (state.canSurvive(worldGenLevel, reedsPos)) {
                BlockState upperState = state.setValue(ReedsBlock.HALF, DoubleBlockHalf.UPPER);
                BlockPos blockPos3 = reedsPos.above();

                if (worldGenLevel.getBlockState(blockPos3).is(Blocks.AIR)) {
                    worldGenLevel.setBlock(reedsPos, state, 2);
                    worldGenLevel.setBlock(blockPos3, upperState, 2);
                }

                placedAny = true;
            }
        }
        return placedAny;
    }
}
