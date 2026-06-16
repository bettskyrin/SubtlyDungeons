package net.meander.subtlyd.world.level.levelgen.feature;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class FeatureSD {
    public static final ReedsFeature REEDS = register("reeds", new ReedsFeature(ProbabilityFeatureConfiguration.CODEC));

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, Util.identifier(name), feature);
    }

    public static void registration() {}
}
