package net.meander.subtlyd.world.level.levelgen.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.Feature;

/**
 * @see net.minecraft.world.level.levelgen.feature.FeatureTypes
 */
public class FeatureTypesSD {
    public static MapCodec<? extends Feature> registration(final Registry<MapCodec<? extends Feature>> registry) {
        return Registry.register(registry, "reeds", ReedsFeature.CODEC);
    }
}
