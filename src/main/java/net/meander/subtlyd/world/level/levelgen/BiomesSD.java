package net.meander.subtlyd.world.level.levelgen;

import net.meander.subtlyd.util.Util;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.meander.subtlyd.world.level.levelgen.feature.ReedsFeature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BiomesSD {
    public static void init() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SWAMP), GenerationStep.Decoration.VEGETAL_DECORATION, ReedsFeature.REEDS_PLACED_FEATURE);
    }

    public static <C extends FeatureConfiguration, F extends Feature<C>> F register(String string, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, Util.identifier(string), feature);
    }
}
