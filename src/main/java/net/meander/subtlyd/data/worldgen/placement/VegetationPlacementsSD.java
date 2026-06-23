package net.meander.subtlyd.data.worldgen.placement;

import net.meander.subtlyd.data.worldgen.features.AquaticFeaturesSD;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

/**
 * @see net.minecraft.data.worldgen.placement.AquaticPlacements
 */
public class VegetationPlacementsSD {
    public static final ResourceKey<PlacedFeature> REEDS = PlacementUtilsSD.createKey("reeds");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<Feature> configuredFeatures = context.lookup(Registries.FEATURE);

        context.register(REEDS, new PlacedFeature(
                configuredFeatures.getOrThrow(AquaticFeaturesSD.REEDS),
                List.of(
                        CountPlacement.of(200),
                        RarityFilter.onAverageOnceEvery(1),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_TOP_SOLID,
                        BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                                BlockPredicate.matchesBlocks(Direction.UP.getUnitVec3i(), List.of(Blocks.WATER)),
                                BlockPredicate.matchesBlocks(Direction.UP.getUnitVec3i().above(), List.of(Blocks.AIR))
                        )),
                        BiomeFilter.biome()
                )));
    }
}