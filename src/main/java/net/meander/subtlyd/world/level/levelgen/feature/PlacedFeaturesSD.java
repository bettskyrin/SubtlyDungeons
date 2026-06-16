package net.meander.subtlyd.world.level.levelgen.feature;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class PlacedFeaturesSD {
    public static final ResourceKey<PlacedFeature> FOREST_ROCK_SPARSE = ResourceKey.create(Registries.PLACED_FEATURE, Util.identifier("forest_rock_sparse"));
    public static final ResourceKey<PlacedFeature> REEDS = ResourceKey.create(Registries.PLACED_FEATURE, Util.identifier("reeds"));


    /**
     * Bootstraps the placed features into the dynamic registry during Data Generation.
     * This avoids statically constructing features at compile time, adhering to classic Mojang architecture.
     */
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> reedsConfigured = configuredFeatures.getOrThrow(ConfiguredFeaturesSD.REEDS);
        Holder<ConfiguredFeature<?, ?>> forestRockConfigured = configuredFeatures.getOrThrow(ConfiguredFeaturesSD.FOREST_ROCK);

        context.register(FOREST_ROCK_SPARSE, new PlacedFeature(
                forestRockConfigured,
                List.of(
                        RarityFilter.onAverageOnceEvery(16),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                        BiomeFilter.biome()
                )
        ));

        context.register(REEDS, new PlacedFeature(
                reedsConfigured,
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
        ));
    }
}