package net.meander.subtlyd.data.worldgen.features;

import net.meander.subtlyd.world.level.levelgen.feature.ReedsFeature;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;

/**
 * @see net.minecraft.data.worldgen.features.AquaticFeatures
 */
public class AquaticFeaturesSD {
    public static final ResourceKey<Feature> REEDS = FeatureUtilsSD.createKey("reeds");

    public static void bootstrap(BootstrapContext<Feature> context) {
        context.register(REEDS, new ReedsFeature(1.0F));
    }
}