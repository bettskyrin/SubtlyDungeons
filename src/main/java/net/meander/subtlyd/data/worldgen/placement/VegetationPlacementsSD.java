package net.meander.subtlyd.data.worldgen.placement;

import net.meander.subtlyd.data.worldgen.features.VegetationFeaturesSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class VegetationPlacementsSD {
    public static final ResourceKey<PlacedFeature> REEDS = ResourceKey.create(Registries.PLACED_FEATURE, Util.identifier("reeds"));

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(REEDS, new PlacedFeature(
                configuredFeatures.getOrThrow(VegetationFeaturesSD.REEDS),
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
                )));
    }
}