package net.meander.subtlyd.world.level.levelgen.feature;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.block.ReedsBlock;
import com.mojang.serialization.Codec;
import net.meander.subtlyd.world.level.levelgen.BiomesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ReedsFeature extends Feature<ProbabilityFeatureConfiguration> {
    public ReedsFeature(Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec);
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> REEDS_CONFIGURED_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Util.identifier("reeds_configured_feature"));
    public static final ResourceKey<PlacedFeature> REEDS_PLACED_FEATURE = ResourceKey.create(Registries.PLACED_FEATURE, Util.identifier("reeds_placed_feature"));
    public static final ReedsFeature REEDS = BiomesSD.register("reeds", new ReedsFeature(ProbabilityFeatureConfiguration.CODEC));
    public static final ConfiguredFeature<?, ?> REEDS_CONFIGURED = new ConfiguredFeature<>(REEDS, new ProbabilityFeatureConfiguration(1F));
    public static final PlacedFeature REEDS_PLACED =
            new PlacedFeature(
                    Holder.direct(REEDS_CONFIGURED),
                    List.of(
                            CountPlacement.of(200),
                            RarityFilter.onAverageOnceEvery(1),
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_TOP_SOLID,
                            BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                                    BlockPredicate.matchesBlocks(Direction.UP.getUnitVec3i(), Blocks.WATER),
                                    BlockPredicate.matchesBlocks(Direction.UP.getUnitVec3i().above(), Blocks.AIR)
                            )),
                            BiomeFilter.biome()
                    )
            );

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
                    if (worldGenLevel.getBlockState(blockPos3).is(Blocks.AIR)) {
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
