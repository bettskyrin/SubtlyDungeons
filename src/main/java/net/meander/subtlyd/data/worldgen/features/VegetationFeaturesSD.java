package net.meander.subtlyd.data.worldgen.features;

import net.meander.subtlyd.world.block.BlocksSD;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

/**
 * @see net.minecraft.data.worldgen.features.VegetationFeatures
 */
public class VegetationFeaturesSD {
    public static final ResourceKey<Feature> PERSE_WILDFLOWER = FeatureUtilsSD.createKey("perse_wildflower");

    public static void bootstrap(BootstrapContext<Feature> context) {
        context.register(PERSE_WILDFLOWER, new SimpleBlockFeature(new WeightedStateProvider(VegetationFeatures.flowerBedPatchBuilder(BlocksSD.PERSE_WILDFLOWERS))));
    }
}