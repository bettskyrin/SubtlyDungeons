package com.kr1s1s.subtlyd.world.level.levelgen;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class BiomesSD {
    public static final ResourceKey<ConfiguredFeature<?, ?>> REEDS_CONFIGURED_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, SubtlyDungeons.resourceLocation("reeds_configured_feature"));
    public static final ResourceKey<PlacedFeature> REEDS_PLACED_FEATURE = ResourceKey.create(Registries.PLACED_FEATURE, SubtlyDungeons.resourceLocation("reeds_placed_feature"));
    public static final ReedsFeature REEDS = register("reeds", new ReedsFeature(ProbabilityFeatureConfiguration.CODEC));
    public static final ConfiguredFeature<?, ?> REEDS_CONFIGURED = new ConfiguredFeature<>(REEDS, new ProbabilityFeatureConfiguration(1F));
    public static final PlacedFeature REEDS_PLACED =
            new PlacedFeature(
                    Holder.direct(REEDS_CONFIGURED),
                    List.of(
                            CountPlacement.of(30),
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_TOP_SOLID,
                            BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                                    BlockPredicate.matchesBlocks(Blocks.WATER),
                                    BlockPredicate.matchesBlocks(Direction.UP.getUnitVec3i(), Blocks.AIR),
                                    BlockPredicate.solid(Direction.DOWN.getUnitVec3i()))),
                            BiomeFilter.biome()
                    )
            );
    public static void init() {
        //BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SWAMP), GenerationStep.Decoration.VEGETAL_DECORATION, REEDS_PLACED_FEATURE);
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String string, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, string, feature);
    }
}
