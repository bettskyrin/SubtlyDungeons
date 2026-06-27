package net.meander.subtlyd.data.worldgen.placement;

import net.meander.subtlyd.data.worldgen.features.VegetationFeaturesSD;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
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
    public static final ResourceKey<PlacedFeature> PATCH_PERSE_WILDFLOWER = PlacementUtilsSD.createKey("patch_perse_wildflower");
    public static final ResourceKey<PlacedFeature> PATCH_GRASS_BIRCH_FOREST = PlacementUtilsSD.createKey("patch_grass_birch_forest");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<Feature> configuredFeatures = context.lookup(Registries.FEATURE);

        context.register(PERSE_WILDFLOWERS_DAPPLED_FOREST, new PlacedFeature(
                configuredFeatures.getOrThrow(VegetationFeaturesSD.PERSE_WILDFLOWER),
                List.of(
                        RarityFilter.onAverageOnceEvery(8),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        CountPlacement.of(32),
                        RandomOffsetPlacement.ofTriangle(6, 2),
                        BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE),
                        BiomeFilter.biome())
                )
        );

        context.register(PERSE_WILDFLOWERS_SWAMP, new PlacedFeature(
                configuredFeatures.getOrThrow(VegetationFeaturesSD.PERSE_WILDFLOWER),
                List.of(
                        CountPlacement.of(2),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                        BiomeFilter.biome())
                )
        );

        context.register(PATCH_PERSE_WILDFLOWER, new PlacedFeature(
                configuredFeatures.getOrThrow(VegetationFeaturesSD.PERSE_WILDFLOWER),
                List.of(
                        RarityFilter.onAverageOnceEvery(8),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        CountPlacement.of(32),
                        RandomOffsetPlacement.ofTriangle(6, 3),
                        BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE),
                        BiomeFilter.biome()))
        );

        context.register(PATCH_GRASS_BIRCH_FOREST, new PlacedFeature(
                configuredFeatures.getOrThrow(VegetationFeatures.GRASS),
                Util.copyAndAdd(
                        VegetationPlacements.worldSurfaceSquaredWithCount(4),
                        CountPlacement.of(16),
                        RandomOffsetPlacement.ofTriangle(7, 3),
                        BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)
                )
        ));
    }
}