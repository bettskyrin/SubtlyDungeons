package net.meander.subtlyd.data.worldgen.features;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.level.levelgen.feature.FeatureTypesSD;
import net.meander.subtlyd.world.level.levelgen.feature.trunkplacers.TrunkPlacerTypeSD;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;

/**
 * @see net.minecraft.data.worldgen.features.FeatureUtils
 */
public class FeatureUtilsSD {
    public static void bootstrap() {
        FeatureTypesSD.bootstrap(BuiltInRegistries.FEATURE_TYPE);
        TrunkPlacerTypeSD.initalize();
    }

    public static ResourceKey<Feature> createKey(final String name) {
        return ResourceKey.create(Registries.FEATURE, Util.identifier(name));
    }
}
