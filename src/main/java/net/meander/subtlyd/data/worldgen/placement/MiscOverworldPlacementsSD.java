package net.meander.subtlyd.data.worldgen.placement;

import net.meander.subtlyd.data.worldgen.features.MiscOverworldFeaturesSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class MiscOverworldPlacementsSD {
    public static final ResourceKey<PlacedFeature> FOREST_ROCK_SPARSE = ResourceKey.create(Registries.PLACED_FEATURE, Util.identifier("forest_rock_sparse"));
    public static final ResourceKey<PlacedFeature> MUD_PATCH = ResourceKey.create(Registries.PLACED_FEATURE, Util.identifier("mud_patch"));

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

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
                        BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.UP.getUnitVec3i(), Blocks.WATER)),
                        BiomeFilter.biome()
                )
        ));
    }
}
