package net.meander.subtlyd.data.worldgen.features;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.level.levelgen.feature.FeatureSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class VegetationFeaturesSD {
    public static final ResourceKey<ConfiguredFeature<?, ?>> REEDS = ResourceKey.create(Registries.CONFIGURED_FEATURE, Util.identifier("reeds"));

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(REEDS, new ConfiguredFeature<>(FeatureSD.REEDS, new ProbabilityFeatureConfiguration(1.0F)));
    }
}