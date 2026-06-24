package net.meander.subtlyd.data.worldgen.placement;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

/**
 * @see net.minecraft.data.worldgen.placement.MiscOverworldPlacements
 */
public class MiscOverworldPlacementsSD {
    public static final ResourceKey<PlacedFeature> FOREST_ROCK_SPARSE = PlacementUtilsSD.createKey("forest_rock_sparse");

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
    }
}
