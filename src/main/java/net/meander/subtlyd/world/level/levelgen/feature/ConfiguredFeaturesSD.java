package net.meander.subtlyd.world.level.levelgen.feature;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class ConfiguredFeaturesSD {
    public static final ResourceKey<ConfiguredFeature<?, ?>> REEDS = ResourceKey.create(Registries.CONFIGURED_FEATURE, Util.identifier("reeds"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_ROCK = ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.withDefaultNamespace("forest_rock"));

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(REEDS, new ConfiguredFeature<>(FeatureSD.REEDS, new ProbabilityFeatureConfiguration(1.0F)));
    }
}