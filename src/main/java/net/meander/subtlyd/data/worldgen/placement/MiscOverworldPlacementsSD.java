package net.meander.subtlyd.data.worldgen.placement;

import net.meander.subtlyd.data.worldgen.features.MiscOverworldFeaturesSD;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

/**
 * @see net.minecraft.data.worldgen.placement.MiscOverworldPlacements
 */
public class MiscOverworldPlacementsSD {
    public static final ResourceKey<PlacedFeature> FOREST_ROCK_SPARSE = PlacementUtilsSD.createKey("forest_rock_sparse");
    public static final ResourceKey<PlacedFeature> MUD_PATCH = PlacementUtilsSD.createKey("mud_patch");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<Feature> configuredFeatures = context.lookup(Registries.FEATURE);

        context.register(FOREST_ROCK_SPARSE, new PlacedFeature(
                configuredFeatures.getOrThrow(MiscOverworldFeatures.FOREST_ROCK),
                List.of(
                        RarityFilter.onAverageOnceEvery(16),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                        BiomeFilter.biome()
                )));

        context.register(MUD_PATCH, new PlacedFeature(
                configuredFeatures.getOrThrow(MiscOverworldFeaturesSD.MUD_PATCH),
                List.of(
                        CountPlacement.of(8),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.UP.getUnitVec3i(), List.of(Blocks.WATER))),
                        BiomeFilter.biome()
                )
        ));
    }
}
