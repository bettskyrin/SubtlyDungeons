package net.meander.subtlyd.data.worldgen.placement;

import net.meander.subtlyd.data.worldgen.features.TreeFeaturesSD;
import net.meander.subtlyd.data.worldgen.features.VegetationFeaturesSD;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

/**
 * @see net.minecraft.data.worldgen.placement.VegetationPlacements
 */
public class VegetationPlacementsSD {
    public static final ResourceKey<PlacedFeature> PERSE_WILDFLOWERS_DAPPLED_FOREST = PlacementUtilsSD.createKey("perse_wildflower_dappled_forest");
    public static final ResourceKey<PlacedFeature> PERSE_WILDFLOWERS_SWAMP = PlacementUtilsSD.createKey("perse_wildflower_swamp");
    public static final ResourceKey<PlacedFeature> PATCH_GRASS_BIRCH_FOREST = PlacementUtilsSD.createKey("patch_grass_birch_forest");
    public static final ResourceKey<PlacedFeature> WILDFLOWERS_BIRCH_FOREST = PlacementUtilsSD.createKey("wildflowers_birch_forest");
    public static final ResourceKey<PlacedFeature> WILDFLOWERS_MEADOW = PlacementUtilsSD.createKey("wildflowers_meadow");
    public static final ResourceKey<PlacedFeature> DARK_FOREST_VEGETATION = PlacementUtilsSD.createKey("dark_forest_vegetation");
    public static final ResourceKey<PlacedFeature> BAOBAB = PlacementUtilsSD.createKey("baobab");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<Feature> configuredFeatures = context.lookup(Registries.FEATURE);
        PlacementModifier treeThreshold = SurfaceWaterDepthFilter.forMaxDepth(0);

        context.register(PERSE_WILDFLOWERS_DAPPLED_FOREST,
                new PlacedFeature(configuredFeatures.getOrThrow(VegetationFeaturesSD.PERSE_WILDFLOWER),
                    List.of(
                            RarityFilter.onAverageOnceEvery(8),
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP,
                            CountPlacement.of(32),
                            OffsetPlacement.ofTriangle(6, 2),
                            BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE),
                            BiomeFilter.biome()
                    )
                )
        );

        context.register(PERSE_WILDFLOWERS_SWAMP,
                new PlacedFeature(configuredFeatures.getOrThrow(VegetationFeaturesSD.PERSE_WILDFLOWER),
                    List.of(
                            CountPlacement.of(2),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                            BiomeFilter.biome()
                    )
                )
        );

        context.register(WILDFLOWERS_BIRCH_FOREST,
                new PlacedFeature(configuredFeatures.getOrThrow(VegetationFeaturesSD.WILDFLOWER),
                    List.of(
                            CountPlacement.of(3),
                            RarityFilter.onAverageOnceEvery(2),
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP,
                            BiomeFilter.biome(),
                            CountPlacement.of(64),
                            OffsetPlacement.ofTriangle(6, 2),
                            BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)
                    )
                )
        );

        context.register(WILDFLOWERS_MEADOW,
                new PlacedFeature(configuredFeatures.getOrThrow(VegetationFeaturesSD.WILDFLOWER),
                List.of(
                        CountPlacement.of(3),
                        RarityFilter.onAverageOnceEvery(2),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome(),
                        CountPlacement.of(64),
                        OffsetPlacement.ofTriangle(6, 2),
                        BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE))
                )
        );

        context.register(PATCH_GRASS_BIRCH_FOREST,
                new PlacedFeature(configuredFeatures.getOrThrow(VegetationFeatures.GRASS),
                    Util.copyAndAdd(
                            VegetationPlacements.worldSurfaceSquaredWithCount(4),
                            CountPlacement.of(16),
                            OffsetPlacement.ofTriangle(7, 3),
                            BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)
                    )
                )
        );

        context.register(DARK_FOREST_VEGETATION,
                new PlacedFeature(configuredFeatures.getOrThrow(VegetationFeaturesSD.DARK_FOREST_VEGETATION),
                    List.of(
                            CountPlacement.of(32),
                            InSquarePlacement.spread(),
                            treeThreshold,
                            PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                            BiomeFilter.biome()
                    )
                )
        );

        context.register(BAOBAB,
                new PlacedFeature(
                    configuredFeatures.getOrThrow(TreeFeaturesSD.BAOBAB),
                    VegetationPlacements.treePlacement(
                            PlacementUtils.countExtra(0, 0.02F, 1),
                            Blocks.ACACIA_SAPLING
                    )
                )
        );
    }
}